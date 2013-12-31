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

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Utility class for random protocol methods
 */
public class CryptoUtil {
    /**
     * Creates a checksum based on the payload
     * @param request The payload
     * @param encryptionMethod The encryption method to use
     * @return The first four bytes of the checksum
     * @throws java.lang.IllegalArgumentException If the encryption method does not exist
     */
    public static byte[] createChecksum(byte[] request, String encryptionMethod) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(encryptionMethod);

            byte[] firstRound = messageDigest.digest(request);
            byte[] secondRound = messageDigest.digest(firstRound);

            byte[] firstFourBytes = new byte[4];
            System.arraycopy(secondRound, 0, firstFourBytes, 0, 4);
            ByteBuffer byteBuffer = ByteBuffer.wrap(firstFourBytes);

            return byteBuffer.array();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(
                    String.format("The encryption method, %s does not exist", encryptionMethod));
        }
    }

    /**
     * Generates a random nonce
     * @return The nonce
     */
    public static byte[] generateNonce() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] random = new byte[8];
        secureRandom.nextBytes(random);

        return random;
    }
}
