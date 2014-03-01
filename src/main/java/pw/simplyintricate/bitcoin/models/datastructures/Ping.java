package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;

/**
 * lib-bitcoinj
 * Copyright (c) 2014, Stephen Liang, All rights reserved.
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
public class Ping {
    private UnsignedInteger nonce;

    public Ping(UnsignedInteger nonce) {
        this.nonce = nonce;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.writeInt(nonce.intValue());

        return writer.toByteArray();
    }

    public static Ping fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        UnsignedInteger nonce = UnsignedInteger.fromIntBits(reader.readInt());

        return new Ping(nonce);
    }
}
