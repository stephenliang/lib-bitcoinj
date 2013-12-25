package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.IOException;

/**
 * Created by Stephen on 12/27/13.
 */
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
}
