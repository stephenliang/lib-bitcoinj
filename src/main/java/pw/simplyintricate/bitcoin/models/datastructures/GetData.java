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
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;

public class GetData {
    private final VariableInteger length;
    private final InventoryVector[] inventoryVectors;

    public GetData(VariableInteger length, InventoryVector[] inventoryVectors) {
        this.length = length;
        this.inventoryVectors = inventoryVectors;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();
        int intLength = length.getValue().intValue();

        writer.write(length.toByteArray());

        for (int i = 0; i < intLength; i++) {
            writer.write(inventoryVectors[i].toByteArray());
        }

        return writer.toByteArray();
    }

    public static GetData fromInputStream(LittleEndianDataInputStream inputStream) throws IOException {
        VariableInteger length = VariableInteger.fromInputStream(inputStream);
        int intLength = length.getValue().intValue();
        InventoryVector[] inventoryVectors = new InventoryVector[intLength];

        for (int i=0; i< intLength; i++) {
            inventoryVectors[i] = InventoryVector.fromInputStream(inputStream);
        }

        return new GetData(length, inventoryVectors);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
