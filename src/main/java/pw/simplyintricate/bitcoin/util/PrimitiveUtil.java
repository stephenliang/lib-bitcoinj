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

package pw.simplyintricate.bitcoin.util;

import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;

public class PrimitiveUtil {
    private static final int COMMAND_LENGTH = 12;

    public static byte[] convertToNullTerminatedString(String command) throws UnsupportedEncodingException {
        byte[] commandBytes = command.getBytes("ISO-8859-1");
        int proposedLength = commandBytes.length+1;

        if (proposedLength < COMMAND_LENGTH) {
            proposedLength = COMMAND_LENGTH;
        }

        byte[] cStyleString = new byte[proposedLength];
        System.arraycopy(commandBytes, 0, cStyleString, 0, commandBytes.length);

        return cStyleString;
    }

    public static String convertIpv4AddressToString(byte[] ipv4Address) {
        if (ipv4Address.length != 4) {
            throw new IllegalArgumentException("The input is too large, it must be of size 4. " +
                    "The size is " + ipv4Address.length);
        }

        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < ipv4Address.length; i++) {
            int currentOctet = ipv4Address[i] & 0xFF;

            sb.append(currentOctet);

            if (i != 3) {
                sb.append(".");
            }
        }

        return sb.toString();
    }

    public static int signedByteToUnsignedByte(byte signedByte) {
        return (signedByte & 0xFF);
    }

    public static String byteArrayToString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder("");

        for (byte b : byteArray) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public static byte[] reverseArray(byte[] bytes) {
        byte[] buffer = ArrayUtils.clone(bytes);
        ArrayUtils.reverse(buffer);
        return buffer;
    }
}
