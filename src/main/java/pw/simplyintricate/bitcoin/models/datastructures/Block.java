package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.util.CryptoUtil;
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
public class Block {
    private final UnsignedInteger version;
    private final byte[] previousBlock;
    private final byte[] merkleRoot;
    private final UnsignedInteger timestamp;
    private final UnsignedInteger difficultyTarget;
    private final UnsignedInteger nonce;
    private final VariableInteger transactionCount;
    private final Tx[] transactions;

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

        writeBlockHeader(writer);
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
        reader.read(previousBlock, 0, 32);
        reader.read(merkleRoot, 0, 32);

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

    public UnsignedInteger getVersion() {
        return version;
    }

    public byte[] getPreviousBlock() {
        return PrimitiveUtil.reverseArray(previousBlock);
    }

    public byte[] getMerkleRoot() {
        return PrimitiveUtil.reverseArray(merkleRoot);
    }

    public UnsignedInteger getTimestamp() {
        return timestamp;
    }

    public UnsignedInteger getDifficultyTarget() {
        return difficultyTarget;
    }

    public UnsignedInteger getNonce() {
        return nonce;
    }

    public VariableInteger getTransactionCount() {
        return transactionCount;
    }

    public Tx[] getTransactions() {
        return transactions;
    }

    public String getHash(CryptoCurrency coin) {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writeBlockHeader(writer);

        byte[] result = writer.toByteArray();
        byte[] doubleHashedResult = CryptoUtil.doubleHash(result, coin.getEncryptionMethod());

        return PrimitiveUtil.byteArrayToString(doubleHashedResult);
    }

    private void writeBlockHeader(HybridByteArrayDataOutput writer) {
        writer.writeInt(version.intValue());
        writer.write(PrimitiveUtil.reverseArray(previousBlock));
        writer.write(PrimitiveUtil.reverseArray(merkleRoot));
        writer.writeInt(timestamp.intValue());
        writer.writeInt(difficultyTarget.intValue());
        writer.writeInt(nonce.intValue());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
