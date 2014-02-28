package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

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
public class TxIn {
    private OutPoint outPoint;
    private VariableInteger scriptLength;
    private byte[] signatureScript;
    private UnsignedInteger sequence;

    public TxIn(OutPoint outPoint, VariableInteger scriptLength, byte[] signatureScript, UnsignedInteger sequence) {
        this.outPoint = outPoint;
        this.scriptLength = scriptLength;
        this.signatureScript = signatureScript;
        this.sequence = sequence;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.write(outPoint.toByteArray());
        writer.write(scriptLength.toByteArray());
        writer.write(signatureScript);
        writer.write(sequence.intValue());

        return writer.toByteArray();
    }

    public static TxIn fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        OutPoint outPoint = OutPoint.fromInputStream(reader);
        VariableInteger scriptLength = VariableInteger.fromInputStream(reader);
        int scriptLengthInt = scriptLength.getValue().intValue();
        byte[] signatureScript = new byte[scriptLengthInt];
        reader.read(signatureScript, 0, scriptLengthInt);
        int sequence = reader.readInt();

        return new TxIn(outPoint, scriptLength, signatureScript, UnsignedInteger.fromIntBits(sequence));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
