package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;
import pw.simplyintricate.bitcoin.util.PrimitiveUtil;

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
public class OutPoint {
    private byte[] hash;
    private UnsignedInteger index;

    public OutPoint(byte[] hash, UnsignedInteger index) {
        Validate.isTrue(hash.length == 32);

        this.hash = hash;
        this.index = index;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.write(hash);
        writer.write(index.intValue());

        return writer.toByteArray();
    }

    public static OutPoint fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        byte[] hash = new byte[32];
        reader.read(hash, 0, 32);
        hash = PrimitiveUtil.bigEndianToLittleEndian(hash);
        int index = reader.readInt();

        return new OutPoint(hash, UnsignedInteger.fromIntBits(index));
    }

    public byte[] getHash() {
        return hash;
    }

    public UnsignedInteger getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
