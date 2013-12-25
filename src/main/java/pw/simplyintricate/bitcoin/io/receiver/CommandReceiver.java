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

package pw.simplyintricate.bitcoin.io.receiver;

import com.google.common.primitives.UnsignedInteger;
import pw.simplyintricate.bitcoin.io.receiver.factory.CommandFactory;
import pw.simplyintricate.bitcoin.io.receiver.handler.CommandHandler;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.receiver.CryptoCoinConnectionException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.util.Arrays;

public class CommandReceiver implements Runnable {
    private static final int MAGIC_HEADER_SIZE = 4;

    private final InputStream connectionInputStream;
    private final PushbackInputStream pushbackInputStream;
    private final CryptoCurrency coin;
    private final CommandFactory commandFactory;

    public CommandReceiver(Socket connectionSocket, CryptoCurrency coin, CommandFactory commandFactory) {
        try {
            this.connectionInputStream = connectionSocket.getInputStream();
        } catch(IOException e) {
            throw new CryptoCoinConnectionException("Failed to connect to remote endpoint!", e);
        }

        pushbackInputStream = new PushbackInputStream(connectionInputStream, MAGIC_HEADER_SIZE);
        this.coin = coin;
        this.commandFactory = commandFactory;
    }

    @Override
    public void run() {
        try {
            byte[] inputBuffer = new byte[4];
            int read;
            while ( (read = pushbackInputStream.read(inputBuffer, 0, 4)) != -1 ) {
                pushbackInputStream.unread(inputBuffer);

                if (Arrays.equals(coin.getMagicHeader(), inputBuffer)) {
                    System.out.println("Found the magic header");
                    try {
                        processCommand(new DataInputStream(pushbackInputStream));
                    } catch(RuntimeException e) {
                        // keep on chuggin
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("done");
        } catch(IOException e) {
            throw new CryptoCoinConnectionException("Failed to connect to remote endpoint!", e);
        }
    }

    private void processCommand(DataInputStream dataInputStream) throws IOException {
        dataInputStream.skip(4); // skip the magic header we already read it
        byte[] command = new byte[12];
        dataInputStream.read(command, 0, 12);

        int intLength = dataInputStream.readInt();
        UnsignedInteger length = UnsignedInteger.fromIntBits(intLength);
        byte[] checksum = new byte[4];
        dataInputStream.read(checksum, 0, 4);

        if (length.longValue() > Integer.MAX_VALUE) {
            System.out.println("This value is too long");
        }

        CommandHandler commandHandler = commandFactory.getCommandHandler(command);
        commandHandler.processBytes(dataInputStream, coin);
    }
}
