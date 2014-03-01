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
public class Block {
    private UnsignedInteger version;
    private byte[] previousBlock;
    private byte[] merkleRoot;
    private UnsignedInteger timestamp;
    private UnsignedInteger difficultyTarget;
    private UnsignedInteger nonce;
    private VariableInteger transactionCount;
    private Tx[] transactions;

    public Block(UnsignedInteger version,
                 byte[] previousBlock,
                 byte[] merkleRoot,
                 UnsignedInteger timestamp,
                 UnsignedInteger difficultyTarget,
                 UnsignedInteger nonce,
                 VariableInteger transactionCount,
                 Tx[] transactions) {
        this.version = version;
        this.previousBlock = previousBlock;
        this.merkleRoot = merkleRoot;
        this.timestamp = timestamp;
        this.difficultyTarget = difficultyTarget;
        this.nonce = nonce;
        this.transactionCount = transactionCount;
        this.transactions = transactions;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.writeInt(version.intValue());
        writer.write(previousBlock);
        writer.write(merkleRoot);
        writer.writeInt(timestamp.intValue());
        writer.writeInt(difficultyTarget.intValue());
        writer.writeInt(nonce.intValue());
        writer.write(transactionCount.toByteArray());

        for (Tx tx: transactions) {
            writer.write(tx.toByteArray());
        }

        return writer.toByteArray();
    }

    public static Block fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        UnsignedInteger version = UnsignedInteger.fromIntBits(reader.readInt());
        byte[] previousBlock = new byte[32];
        byte[] merkleRoot = new byte[32];
        UnsignedInteger timestamp = UnsignedInteger.fromIntBits(reader.readInt());
        UnsignedInteger difficultyTarget = UnsignedInteger.fromIntBits(reader.readInt());
        UnsignedInteger nonce = UnsignedInteger.fromIntBits(reader.readInt());
        VariableInteger transactionCount = VariableInteger.fromInputStream(reader);
        int transactionLength = transactionCount.getValue().intValue();
        Tx[] txes = new Tx[transactionLength];

        for (int i = 0; i < transactionLength; i++) {
            txes[i] = Tx.fromInputStream(reader);
        }

        return new Block(version, previousBlock, merkleRoot, timestamp, difficultyTarget, nonce, transactionCount, txes);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}