/*
 * lib-bitcoin
 * Copyright (c) 2014, Stephen Liang, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package pw.simplyintricate.bitcoin.io.receiver.handler.impl;

import com.google.common.io.LittleEndianDataInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pw.simplyintricate.bitcoin.io.receiver.factory.CommandFactory;
import pw.simplyintricate.bitcoin.io.receiver.handler.CommandHandler;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.models.datastructures.Addr;
import pw.simplyintricate.bitcoin.models.datastructures.NetworkAddress;
import pw.simplyintricate.bitcoin.receiver.CryptoCoinReceiver;
import pw.simplyintricate.bitcoin.util.PrimitiveUtil;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by Stephen on 12/28/13.
 */
@Component
public class AddrHandlerImpl implements CommandHandler {
    @Autowired private CommandFactory commandFactory;

    @Override
    public void processBytes(DataInputStream inputStream, CryptoCurrency coin) {
        try {
            LittleEndianDataInputStream reader = new LittleEndianDataInputStream(inputStream);
            Addr addr = Addr.fromInputStream(reader);

            System.out.println("Got an addr!" + addr);

            int intLength = addr.getCount().getValue().intValue();
            NetworkAddress[] networkAddresses = addr.getNetworkAddresses();

            for (int i = 0; i < intLength; i++) {
                NetworkAddress networkAddress = networkAddresses[i];
                String ipAddress = PrimitiveUtil.convertIpv4AddressToString(networkAddress.getIpAddress());
                int port = 8333;

                System.out.println("Connecting to " + ipAddress + ":" + port);
                CryptoCoinReceiver newConnection = new CryptoCoinReceiver(ipAddress, port, coin, commandFactory);
                newConnection.sendVersion();
            }

        } catch(IOException e) {
            throw new IllegalArgumentException("There was an error while parsing the payload", e);
        }
    }
}
