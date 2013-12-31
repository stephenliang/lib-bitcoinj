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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.EndianUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkAddress {
    private final UnsignedInteger services; //unsigned 64-bit int
    private final byte[] ipAddress;
    private final int port;
    private UnsignedInteger time;

    public UnsignedInteger getServices() {
        return services;
    }

    public byte[] getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public UnsignedInteger getTime() {
        return time;
    }

    public NetworkAddress(UnsignedInteger services, byte[] ipAddress, int port, UnsignedInteger time) {
        this.ipAddress = ipAddress;
        this.services = services;
        this.port = port;
        this.time = time;
    }

    public NetworkAddress(UnsignedInteger services, String ipAddress, int port) {
        try {
            InetAddress ip = InetAddress.getByName(ipAddress);

            this.ipAddress = ip.getAddress();
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("The ip address is invalid!", e);
        }

        this.services = services;
        this.port = port;
    }

    public NetworkAddress(UnsignedInteger services, byte[] ipAddress, int port) {
        this.services = services;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public byte[] toByteArray() {
        ByteArrayDataOutput writer = ByteStreams.newDataOutput();

        //writer.writeInt(EndianUtils.swapInteger(time));
        writer.writeLong(EndianUtils.swapLong(services.longValue()));
        // write the two ipv4 to ipv6 pads
        for (int i = 0; i < 10; i++) {
            writer.writeByte(0);
        }
        writer.writeByte(0xFF);
        writer.writeByte(0xFF);
        writer.write(ipAddress);
        writer.writeShort(port);

        return writer.toByteArray();
    }

    public static NetworkAddress fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        return fromInputStream(reader, false);
    }

    public static NetworkAddress fromInputStream(LittleEndianDataInputStream reader, boolean readTime) throws IOException {
        int time = -1;
        if (readTime) {
            time = reader.readInt();
        }

        long services = reader.readLong();
        reader.skip(12); // skip ipv6 support :(
        byte[] ipAddress = new byte[4];
        reader.read(ipAddress, 0, 4);
        short port = EndianUtils.swapShort(((short) reader.readUnsignedShort()));

        NetworkAddress networkAddress;
        if (readTime) {
            networkAddress = new NetworkAddress(UnsignedInteger.valueOf(services), ipAddress, port,
                    UnsignedInteger.fromIntBits(time));
        } else {
            networkAddress = new NetworkAddress(UnsignedInteger.valueOf(services), ipAddress, port);
        }

        return networkAddress;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
