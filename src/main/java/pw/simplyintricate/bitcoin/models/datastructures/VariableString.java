package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Stephen on 12/24/13.
 */
public class VariableString {
    private final VariableInteger variableInteger;
    private final byte[] string;

    public VariableString(String string) {
        try {
            byte[] stringBytes = string.getBytes("iso-8859-1");
            byte[] cStyleString = new byte[stringBytes.length+1];

            System.arraycopy(stringBytes, 0, cStyleString, 0, stringBytes.length);
            this.string = cStyleString;
            this.variableInteger = new VariableInteger(UnsignedInteger.fromIntBits(stringBytes.length));
        } catch(UnsupportedEncodingException e) {
            throw new IllegalArgumentException("The string provided is invalid", e);
        }
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.write(variableInteger.toByteArray());
        writer.write(string, 0, string.length-1);

        return writer.toByteArray();
    }

    public static VariableString fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        VariableInteger variableInteger = VariableInteger.fromInputStream(reader);
        int stringSize = variableInteger.getValue().intValue();
        byte[] stringBuffer = new byte[stringSize];
        reader.read(stringBuffer, 0, stringSize);

        VariableString variableString = new VariableString(new String(stringBuffer));

        return variableString;
    }
}
