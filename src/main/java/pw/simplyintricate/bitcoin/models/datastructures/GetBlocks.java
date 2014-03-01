package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
public class GetBlocks {
    private UnsignedInteger version;
    private VariableInteger hashCount;
    private byte[] blockLocatorHashes;
    private byte[] hashStop;

    public GetBlocks(UnsignedInteger version, VariableInteger hashCount, byte[] blockLocatorHashes, byte[] hashStop) {
        this.version = version;
        this.hashCount = hashCount;
        this.blockLocatorHashes = blockLocatorHashes;
        this.hashStop = hashStop;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.writeInt(version.intValue());
        writer.write(hashCount.toByteArray());
        writer.write(blockLocatorHashes);
        writer.write(hashStop);

        return writer.toByteArray();
    }

    public static GetBlocks fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        UnsignedInteger version = UnsignedInteger.fromIntBits(reader.readInt());
        VariableInteger hashCount = VariableInteger.fromInputStream(reader);
        byte[] blockLocatorHashes = new byte[32];
        byte[] hashStop = new byte[32];
        reader.read(blockLocatorHashes, 0, 32);
        reader.read(hashStop, 0, 32);

        return new GetBlocks(version, hashCount, blockLocatorHashes, hashStop);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
