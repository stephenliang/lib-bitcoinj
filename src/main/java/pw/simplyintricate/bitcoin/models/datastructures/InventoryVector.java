/*
 * lib-bitcoin
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

package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;

public class InventoryVector {
    private final UnsignedInteger type;
    private final byte[] hash;

    public InventoryVector(UnsignedInteger type, byte[] hash) {
        this.type = type;
        this.hash = hash;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();
        writer.writeInt(type.intValue());
        writer.write(hash);

        return writer.toByteArray();
    }

    public static InventoryVector fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        int type = reader.readInt();
        byte[] hash = new byte[32];

        reader.read(hash, 0, hash.length);

        return new InventoryVector(UnsignedInteger.fromIntBits(type), hash);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
