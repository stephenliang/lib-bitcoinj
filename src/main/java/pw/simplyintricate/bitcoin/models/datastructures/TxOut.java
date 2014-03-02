package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;
import pw.simplyintricate.bitcoin.util.PrimitiveUtil;

import java.io.IOException;

/**
 * lib-bitcoinj
 * Copyright (c) 2014, Stephen Liang, All rights reserved.
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
public class TxOut {
    private long value;
    private VariableInteger pkScriptLength;
    private byte[] pkScript;

    public TxOut(long value, VariableInteger pkScriptLength, byte[] pkScript) {
        this.value = value;
        this.pkScriptLength = pkScriptLength;
        this.pkScript = pkScript;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.writeLong(value);
        writer.write(pkScriptLength.toByteArray());
        writer.write(PrimitiveUtil.reverseArray(pkScript));

        return writer.toByteArray();
    }

    public static TxOut fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        long value = reader.readLong();
        VariableInteger pkScriptLength = VariableInteger.fromInputStream(reader);
        int pkScriptLengthInt = pkScriptLength.getValue().intValue();
        byte[] pkScript = new byte[pkScriptLengthInt];

        reader.read(pkScript, 0, pkScriptLengthInt);

        return new TxOut(value, pkScriptLength, pkScript);
    }

    public long getValue() {
        return value;
    }

    public VariableInteger getPkScriptLength() {
        return pkScriptLength;
    }

    public byte[] getPkScript() {
        return PrimitiveUtil.reverseArray(pkScript);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
