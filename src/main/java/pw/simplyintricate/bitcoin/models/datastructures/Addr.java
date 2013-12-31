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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;

public class Addr {
    private final VariableInteger count;
    private final NetworkAddress[] networkAddresses;

    public VariableInteger getCount() {
        return count;
    }

    public NetworkAddress[] getNetworkAddresses() {
        return networkAddresses;
    }

    public Addr(VariableInteger count, NetworkAddress[] networkAddresses) {
        this.count = count;
        this.networkAddresses = networkAddresses;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();
        writer.write(count.toByteArray());
        int intLength = count.getValue().intValue();

        for (int i = 0; i < intLength; i++) {
            writer.write(networkAddresses[i].toByteArray());
        }

        return writer.toByteArray();
    }

    public static Addr fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        VariableInteger length = VariableInteger.fromInputStream(reader);
        int intLength = length.getValue().intValue();
        NetworkAddress[] networkAddresses = new NetworkAddress[intLength];

        for (int i = 0; i < intLength; i++) {
            networkAddresses[i] = NetworkAddress.fromInputStream(reader, true);
        }

        return new Addr(length, networkAddresses);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
