package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;

/**
 * Created by Stephen on 12/28/13.
 */
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
