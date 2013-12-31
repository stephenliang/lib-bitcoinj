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

import java.io.IOException;

public final class BlockVersion {
    private final int version; //int32
    private final UnsignedInteger services; //unsigned int64
    private final Long timestamp; //int64
    private final NetworkAddress receivingNodeNetworkAddress;
    private final NetworkAddress emittingNodeNetworkAddress;
    private final byte[] nonce;
    private final VariableString userAgent;
    private final int startingHeight;

    public BlockVersion(int version,
                         UnsignedInteger services,
                         Long timestamp,
                         NetworkAddress receivingNodeNetworkAddress,
                         NetworkAddress emittingNodeNetworkAddress,
                         byte[] nonce,
                         VariableString userAgent,
                         int startingHeight) {
        this.version = version;
        this.services = services;
        this.timestamp = timestamp;
        this.receivingNodeNetworkAddress = receivingNodeNetworkAddress;
        this.emittingNodeNetworkAddress = emittingNodeNetworkAddress;
        this.nonce = nonce;
        this.userAgent = userAgent;
        this.startingHeight = startingHeight;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.writeInt(version);
        writer.writeLong(services.longValue());
        writer.writeLong(timestamp);
        writer.write(receivingNodeNetworkAddress.toByteArray());
        writer.write(emittingNodeNetworkAddress.toByteArray());
        writer.write(nonce);
        writer.write(userAgent.toByteArray());
        writer.writeInt(startingHeight);

        return writer.toByteArray();
    }

    public static BlockVersion fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        int version = reader.readInt();
        long services = reader.readLong();
        long timestamp = reader.readLong();
        NetworkAddress receivingNetworkAddress = NetworkAddress.fromInputStream(reader);
        NetworkAddress emittingNetworkAddress = NetworkAddress.fromInputStream(reader);
        byte[] nonce = new byte[8];
        reader.read(nonce, 0, nonce.length);
        VariableString userAgent = VariableString.fromInputStream(reader);
        int startingHeight = reader.readInt();

        BlockVersion blockVersion = new BlockVersion(version, UnsignedInteger.valueOf(services), timestamp,
                receivingNetworkAddress, emittingNetworkAddress, nonce, userAgent, startingHeight);

        return blockVersion;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
