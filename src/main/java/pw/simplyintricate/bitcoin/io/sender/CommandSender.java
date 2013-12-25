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

package pw.simplyintricate.bitcoin.io.sender;

import com.google.common.primitives.UnsignedInteger;
import pw.simplyintricate.bitcoin.models.Command;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.models.datastructures.Message;
import pw.simplyintricate.bitcoin.receiver.CryptoCoinConnectionException;
import pw.simplyintricate.bitcoin.util.CryptoUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CommandSender {
    private Socket connectionSocket;
    private final DataInputStream connectionInputStream;
    private final DataOutputStream connectionOutputStream;
    private final CryptoCurrency coin;

    public CommandSender(Socket socket, CryptoCurrency coin) {
        try {
            connectionSocket = socket;

            connectionInputStream = new DataInputStream(connectionSocket.getInputStream());
            connectionOutputStream = new DataOutputStream(connectionSocket.getOutputStream());
            this.coin = coin;
        } catch (IOException e) {
            throw new CryptoCoinConnectionException("Failed to connect to remote endpoint", e);
        }
    }

     public void writeCommand(byte[] request, Command command) {
        byte[] checksum = CryptoUtil.createChecksum(request, coin.getEncryptionMethod());

        Message message = new Message.Builder()
                .setCommand(command.getProtocolCommandString())
                .setLength(UnsignedInteger.fromIntBits(request.length))
                .setChecksum(checksum)
                .setPayload(request)
                .setCryptoCurrency(coin)
                .build();

        try {
            byte[] commandArray = message.toByteArray();

            StringBuilder sb = new StringBuilder();
            for (byte b : commandArray) {
                sb.append(String.format("%02X ", b));
            }

            System.out.println(sb.toString());
            connectionOutputStream.write(commandArray); //56 = start of ip addr
        } catch (IOException e) {
            throw new SenderException("Error while sending message to remote endpoint", e);
        }
     }
}
