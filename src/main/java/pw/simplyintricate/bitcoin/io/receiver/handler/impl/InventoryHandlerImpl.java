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
import pw.simplyintricate.bitcoin.io.receiver.handler.CommandHandler;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.models.datastructures.GetData;
import pw.simplyintricate.bitcoin.models.datastructures.Inv;

import java.io.DataInputStream;
import java.io.IOException;

public class InventoryHandlerImpl implements CommandHandler {

    @Override
    public void processBytes(DataInputStream inputStream, CryptoCurrency coin) {
        try {
            LittleEndianDataInputStream reader = new LittleEndianDataInputStream(inputStream);

            Inv inv = Inv.fromInputStream(reader);
            System.out.println("Received an inv" + inv);

            GetData getData = new GetData(inv.getLength(), inv.getInventoryVector());

        } catch(IOException e) {
            throw new IllegalArgumentException("There was an error while parsing the payload", e);
        }
    }
}
