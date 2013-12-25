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
import pw.simplyintricate.bitcoin.io.receiver.CommandReceiver;
import pw.simplyintricate.bitcoin.io.receiver.factory.CommandFactory;
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

public class CryptoCoinReceiver {
    private Socket connectionSocket;
    private final CryptoCurrency coin;
    private final String remoteIpAddress;
    private final int remotePort;
    private final CommandSender commandSender;
    private final CommandReceiver commandReceiver;

    public CryptoCoinReceiver(String ipAddress, int port, CryptoCurrency coin, CommandFactory commandFactory) {
        try {
            connectionSocket = new Socket(ipAddress, port);
            commandSender = new CommandSender(connectionSocket, coin);
            commandReceiver = new CommandReceiver(connectionSocket, coin, commandFactory);

            this.coin = coin;
            this.remoteIpAddress = ipAddress;
            this.remotePort = port;

            new Thread(commandReceiver).start();
        } catch (IOException e) {
            throw new CryptoCoinConnectionException("Failed to connect to remote endpoint", e);
        }
    }

    public void sendVersion() {
        int version = coin.getProtocolVersion();
        UnsignedInteger services = CryptoCoinConstants.SERVICES;
        byte[] nonce = CryptoUtil.generateNonce();
        long unixTime = System.currentTimeMillis() / 1000L;

        NetworkAddress networkAddress = new NetworkAddress(services, remoteIpAddress, remotePort);

        NetworkAddress emittingAddress = new NetworkAddress(services, "0.0.0.0", 0);

        VariableString userAgent = new VariableString(CryptoCoinConstants.USER_AGENT);

        BlockVersion blockVersion = new BlockVersion(version, services, unixTime,
                networkAddress, emittingAddress, nonce, userAgent, 1);

        commandSender.writeCommand(blockVersion.toByteArray(), Command.version);
    }
}
