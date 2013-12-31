/*
 * lib-bitcoin
 * Copyright (c) 2014, Stephen Liang, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package pw.simplyintricate.bitcoin.receiver;

import com.google.common.primitives.UnsignedInteger;
import pw.simplyintricate.bitcoin.io.factory.CommandFactory;
import pw.simplyintricate.bitcoin.io.receiver.CommandReceiver;
import pw.simplyintricate.bitcoin.io.sender.CommandSender;
import pw.simplyintricate.bitcoin.models.Command;
import pw.simplyintricate.bitcoin.models.CryptoCoinConstants;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.models.datastructures.BlockVersion;
import pw.simplyintricate.bitcoin.models.datastructures.NetworkAddress;
import pw.simplyintricate.bitcoin.models.datastructures.VariableString;
import pw.simplyintricate.bitcoin.util.CryptoUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Crypto currency connection manager. Establishes a connection to a node and then instantiates the appropriate
 * sender and receiver threads
 */
public class CryptoConnectionManager implements Runnable {
    private static final String BROADCAST_ADDRESS = "0.0.0.0";
    private static final int BROADCAST_PORT = 0;
    private final CommandFactory commandFactory;
    private Socket connectionSocket;
    private final CryptoCurrency coin;
    private final String remoteIpAddress;
    private final int remotePort;
    private CommandSender commandSender;
    private CommandReceiver commandReceiver;

    /**
     * Constructs a connection manager
     * @param ipAddress The IP Address of the node to connect to
     * @param port The port to connect to
     * @param coin The crypto currency
     * @param commandFactory The command factory providing
     */
    public CryptoConnectionManager(String ipAddress, int port, CryptoCurrency coin, CommandFactory commandFactory) {
        this.coin = coin;
        this.remoteIpAddress = ipAddress;
        this.remotePort = port;
        this.commandFactory = commandFactory;
    }

    private void initiateConnection() throws IOException {
        connectionSocket = new Socket(remoteIpAddress, remotePort);
        commandSender = new CommandSender(connectionSocket, coin);
        commandReceiver = new CommandReceiver(connectionSocket, coin, commandFactory);
    }

    @Override
    public void run() {
        try {
            initiateConnection();

            // Put the command receiver on a separate thread so that we can receive while we send and send while
            // we receive
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(commandReceiver);

            sendVersionCommand();

            // Hang the thread so we don't orphan the receiver thread
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new CryptoCoinConnectionException("Thread interrupted!", e);
        } catch (IOException e) {
            throw new CryptoCoinConnectionException("Failed to connect to remote host!", e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Closes the socket
     */
    private void closeConnection() {
        try {
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the version command to the node signifying we want to begin receiving additional data
     */
    private void sendVersionCommand() {
        int version = coin.getProtocolVersion();
        UnsignedInteger services = CryptoCoinConstants.SERVICES;
        byte[] nonce = CryptoUtil.generateNonce();
        long unixTime = System.currentTimeMillis() / 1000L;

        NetworkAddress networkAddress = new NetworkAddress(services, remoteIpAddress, remotePort);
        NetworkAddress emittingAddress = new NetworkAddress(services, BROADCAST_ADDRESS, BROADCAST_PORT);
        VariableString userAgent = new VariableString(CryptoCoinConstants.USER_AGENT);
        BlockVersion blockVersion = new BlockVersion(version, services, unixTime,
                networkAddress, emittingAddress, nonce, userAgent, 1); //@todo determine starting block

        commandSender.writeCommand(blockVersion.toByteArray(), Command.version);
    }
}
