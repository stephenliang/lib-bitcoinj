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
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;

public class Inv {
    private final VariableInteger length;
    private final InventoryVector[] inventoryVector;

    public Inv(VariableInteger length, InventoryVector[] inventoryVector) {
        this.length = length;
        this.inventoryVector = inventoryVector;
    }

    public static Inv fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        VariableInteger length = VariableInteger.fromInputStream(reader);
        int intLength = length.getValue().intValue();
        InventoryVector[] inventoryVectors = new InventoryVector[intLength];

        for (int i = 0; i < intLength; i++) {
            inventoryVectors[i] = InventoryVector.fromInputStream(reader);
        }

        return new Inv(length, inventoryVectors);
    }

    public VariableInteger getLength() {
        return length;
    }

    public InventoryVector[] getInventoryVector() {
        return inventoryVector;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
