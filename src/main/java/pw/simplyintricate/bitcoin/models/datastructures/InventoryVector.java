package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;

/**
 * Created by Stephen on 12/27/13.
 */
public class InventoryVector {
    private final UnsignedInteger type;
    private final byte[] hash;

    public InventoryVector(UnsignedInteger type, byte[] hash) {
        this.type = type;
        this.hash = hash;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();
        writer.writeInt(type.intValue());
        writer.write(hash);

        return writer.toByteArray();
    }

    public static InventoryVector fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        int type = reader.readInt();
        byte[] hash = new byte[32];

        reader.read(hash, 0, hash.length);

        return new InventoryVector(UnsignedInteger.fromIntBits(type), hash);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
