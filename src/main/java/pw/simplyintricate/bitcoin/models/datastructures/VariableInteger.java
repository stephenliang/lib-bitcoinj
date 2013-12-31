package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;

/**
 * Created by Stephen on 12/25/13.
 */
public class VariableInteger {
    private final UnsignedInteger value;

    public VariableInteger(UnsignedInteger value) {
        this.value = value;
    }

    public byte[] toByteArray() {
        long longValue = value.longValue();
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        if (longValue < 0xfd) {
            writer.writeByte(value.intValue());
        } else if (longValue <= 0xffff) {
            writer.writeByte(0xfd);
            writer.writeShort(value.intValue());
        } else if (longValue <= 0xffffffff ) {
            writer.writeByte(0xfe);
            writer.writeInt(value.intValue());
        } else {
            writer.writeByte(0xff);
            writer.writeLong(value.longValue());
        }

        return writer.toByteArray();
    }

    public UnsignedInteger getValue() {
        return value;
    }

    public static VariableInteger fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        byte determiningSize = reader.readByte();
        UnsignedInteger value;
        if (determiningSize < 0xfd) {
            value = UnsignedInteger.fromIntBits(determiningSize);
        } else if (determiningSize <= 0xffff) {
            int followUpValue = reader.readUnsignedShort();
            value = UnsignedInteger.fromIntBits(followUpValue);
        } else if (determiningSize <= 0xffffffff) {
            int followUpValue = reader.readInt();
            value = UnsignedInteger.valueOf(followUpValue);
        } else {
            long followUpValue = reader.readLong();
            value = UnsignedInteger.valueOf(followUpValue);
        }

        VariableInteger variableInteger = new VariableInteger(value);

        return variableInteger;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
