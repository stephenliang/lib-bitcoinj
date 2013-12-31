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
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 */

package pw.simplyintricate.bitcoin.models.coins;

/**
 * Constants for all crypto currencies available out there
 */
public enum CryptoCurrency {
    BITCOIN(BitcoinConstants.ENCRYPTION_METHOD, BitcoinConstants.MAGIC_HEADER, BitcoinConstants.PROTOCOL_VERSION),
    LITECOIN(LitecoinConstants.ENCRYPTION_METHOD, LitecoinConstants.MAGIC_HEADER, LitecoinConstants.PROTOCOL_VERSION),
    DOGECOIN(DogecoinConstants.ENCRYPTION_METHOD, DogecoinConstants.MAGIC_HEADER, DogecoinConstants.PROTOCOL_VERSION);

    private final String encryptionMethod;
    private final byte[] magicHeader;
    private final int protocolVersion;

    CryptoCurrency(String encryptionMethod, byte[] magicHeader, int protocolVersion) {
        if (magicHeader.length != 4) {
            throw new IllegalArgumentException("The magic header must be of length 4!");
        }

        this.encryptionMethod = encryptionMethod;
        this.magicHeader = magicHeader;
        this.protocolVersion = protocolVersion;
    }

    public String getEncryptionMethod() {
        return encryptionMethod;
    }

    public byte[] getMagicHeader() {
        return magicHeader;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }
}
