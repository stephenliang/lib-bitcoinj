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

package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;
import pw.simplyintricate.bitcoin.util.PrimitiveUtil;

import java.io.IOException;

public class VariableInteger {
    private final UnsignedInteger value;

    public VariableInteger(UnsignedInteger value) {
        this.value = value;
    }

    public byte[] toByteArray() {
        long longValue = value.longValue();
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        if (longValue < 0xfd) {
            writer.writeByte(value.intValue());
        } else if (longValue <= 0xffff) {
            writer.writeByte(0xfd);
            writer.writeShort(value.intValue());
        } else if (longValue <= 0xffffffff ) {
            writer.writeByte(0xfe);
            writer.writeInt(value.intValue());
        } else {
            writer.writeByte(0xff);
            writer.writeLong(value.longValue());
        }

        return writer.toByteArray();
    }

    public UnsignedInteger getValue() {
        return value;
    }

    public static VariableInteger fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        int determiningSize = PrimitiveUtil.signedByteToUnsignedByte(reader.readByte());
        UnsignedInteger value;
        if (determiningSize < 0xfd) {
            value = UnsignedInteger.fromIntBits(determiningSize);
        } else if (determiningSize <= 0xffff) {
            int followUpValue = reader.readUnsignedShort();
            value = UnsignedInteger.fromIntBits(followUpValue);
        } else if (determiningSize <= 0xffffffff) {
            int followUpValue = reader.readInt();
            value = UnsignedInteger.valueOf(followUpValue);
        } else {
            long followUpValue = reader.readLong();
            value = UnsignedInteger.valueOf(followUpValue);
        }

        return new VariableInteger(value);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
