/*
 * lib-bitcoinj
 * Copyright (c) 2014, Stephen Liang, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 */

package pw.simplyintricate.bitcoin.io.receiver;

import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.codec.binary.Hex;
import pw.simplyintricate.bitcoin.io.factory.CommandFactory;
import pw.simplyintricate.bitcoin.io.handler.CommandHandler;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.receiver.CryptoCoinConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Receives a command from the inpustream and processes it
 */
public class CommandReceiver implements Runnable {
    private static final int MAGIC_HEADER_SIZE = 4;
    private static final int COMMAND_SIZE = 12;
    private static final int CHECKSUM_SIZE = 4;

    private final InputStream connectionInputStream;
    private final PushbackInputStream pushbackInputStream;
    private final CryptoCurrency coin;
    private final CommandFactory commandFactory;
    private final Socket connectionSocket;

    private static final Logger LOG = LogManager.getLogger(CommandReceiver.class);

    /**
     * Creates a new command receiver
     * @param connectionSocket The socket containing input and output information
     * @param coin The crypto currency we are using
     * @param commandFactory The command factory to help produce the handlers we need
     */
    public CommandReceiver(Socket connectionSocket, CryptoCurrency coin, CommandFactory commandFactory) {
        try {
            this.connectionInputStream = connectionSocket.getInputStream();
        } catch(IOException e) {
            throw new CryptoCoinConnectionException("Failed to connect to remote endpoint!", e);
        }

        pushbackInputStream = new PushbackInputStream(connectionInputStream, MAGIC_HEADER_SIZE);
        this.coin = coin;
        this.commandFactory = commandFactory;
        this.connectionSocket = connectionSocket;
    }

    /**
     * Starts the thread up by reading from the socket's input stream
     */
    @Override
    public void run() {
        try {
            readFromInputStream();
        } catch(IOException e) {
            throw new CryptoCoinConnectionException("Failed to connect to remote endpoint!", e);
        } finally {
            closeStream();
        }
    }

    /**
     * Closes the input stream
     */
    private void closeStream() {
        try {
            pushbackInputStream.close();
        } catch (IOException e) {
            LOG.error("Error while closing the stream", e);
        }
    }

    /**
     * Reads four bytes into the stream and determines if this is a valid cryptocurrency magic header
     * @throws IOException If an IO Exception occurs
     */
    private void readFromInputStream() throws IOException {
        byte[] inputBuffer = new byte[MAGIC_HEADER_SIZE];
        int read;
        while ( (read = pushbackInputStream.read(inputBuffer, 0, MAGIC_HEADER_SIZE)) != -1 ) {
            processIncomingBytes(inputBuffer);
        }
    }

    /**
     * Checks if the buffer given matches the coin's magic header and processes the command if it does
     * @param inputBuffer The buffer with the four bytes read
     * @throws IOException If an IO Exception occurs
     */
    private void processIncomingBytes(byte[] inputBuffer) throws IOException {
        pushbackInputStream.unread(inputBuffer);
        DataInputStream dataInputStream = new DataInputStream(pushbackInputStream);
        
        if (Arrays.equals(coin.getMagicHeader(), inputBuffer)) {
                processCommand(dataInputStream);
        } else {
            LOG.error("The buffer contains {} which does not match the magic header of {}!",
                    Hex.encodeHex(inputBuffer), Hex.encodeHex(coin.getMagicHeader()));

            pushbackInputStream.skip(MAGIC_HEADER_SIZE);
            while (true) {
                byte byteRead = dataInputStream.readByte();
                byte[] magicHeader = coin.getMagicHeader();

                if (byteRead != magicHeader[0]) {
                    continue;
                }

                byteRead = dataInputStream.readByte();

                if (byteRead != magicHeader[1]) {
                    continue;
                }

                byteRead = dataInputStream.readByte();

                if (byteRead != magicHeader[2]) {
                    continue;
                }

                byteRead = dataInputStream.readByte();

                if (byteRead != magicHeader[3]) {
                    continue;
                }

                byteRead = dataInputStream.readByte();

                if (byteRead != magicHeader[4]) {
                    continue;
                }
                pushbackInputStream.unread(coin.getMagicHeader());
                break;
            }
        }
    }

    /**
     * Processes the command and passes it to the handler for furhter processing
     * @param dataInputStream The input stream from the socket
     * @throws IOException If an IO exception occurs
     */
    private void processCommand(DataInputStream dataInputStream) throws IOException {
        try {
            dataInputStream.skip(MAGIC_HEADER_SIZE); // skip the magic header we already read it
            byte[] command = new byte[COMMAND_SIZE];
            dataInputStream.read(command, 0, COMMAND_SIZE);

            int intLength = dataInputStream.readInt();
            UnsignedInteger length = UnsignedInteger.fromIntBits(intLength);
            byte[] checksum = new byte[4];
            dataInputStream.read(checksum, 0, CHECKSUM_SIZE);

            CommandHandler commandHandler = commandFactory.getCommandHandler(command);
            commandHandler.processBytes(connectionSocket, coin);
        } catch(CryptoCoinConnectionException e) {
            throw new IOException(e);
        } catch(RuntimeException e) {
            LOG.error("Encountered an error while processing the command", e);
        }
    }
}
