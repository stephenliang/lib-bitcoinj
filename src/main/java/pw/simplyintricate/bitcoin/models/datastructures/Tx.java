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
public class Tx {
    private UnsignedInteger version;
    private VariableInteger transactionInCount;
    private TxIn[] txIns;
    private VariableInteger transactionOutCount;
    private TxOut[] txOuts;
    private UnsignedInteger lockTime;

    public Tx(UnsignedInteger version,
              VariableInteger transactionInCount,
              TxIn[] txIns,
              VariableInteger transactionOutCount,
              TxOut[] txOuts,
              UnsignedInteger lockTime) {
        this.version = version;
        this.transactionInCount = transactionInCount;
        this.txIns = txIns;
        this.transactionOutCount = transactionOutCount;
        this.txOuts = txOuts;
        this.lockTime = lockTime;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();
        int transactionInLength = transactionInCount.getValue().intValue();
        int transactionOutLength = transactionOutCount.getValue().intValue();

        writer.writeInt(version.intValue());
        writer.write(transactionInCount.toByteArray());

        for (int i = 0; i < transactionInLength; i++) {
            TxIn txIn = txIns[i];
            writer.write(txIn.toByteArray());
        }

        for (int i = 0; i < transactionOutLength; i++) {
            TxOut txOut = txOuts[i];
            writer.write(txOut.toByteArray());
        }

        writer.writeInt(lockTime.intValue());

        return writer.toByteArray();
    }

    public static Tx fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        UnsignedInteger version = UnsignedInteger.fromIntBits(reader.readInt());
        VariableInteger txInCount = VariableInteger.fromInputStream(reader);
        int txInLength = txInCount.getValue().intValue();
        TxIn[] txIns = new TxIn[txInLength];

        for (int i = 0; i < txInLength; i++) {
            txIns[i] = TxIn.fromInputStream(reader);
        }

        VariableInteger txOutCount = VariableInteger.fromInputStream(reader);
        int txOutLength = txOutCount.getValue().intValue();

        TxOut[] txOuts = new TxOut[txOutLength];

        for (int i =0; i <txOutLength; i++) {
            txOuts[i] = TxOut.fromInputStream(reader);
        }
        UnsignedInteger lockTime = UnsignedInteger.fromIntBits(reader.readInt());

        return new Tx(version, txInCount, txIns, txOutCount, txOuts, lockTime);
    }

    public UnsignedInteger getVersion() {
        return version;
    }

    public VariableInteger getTransactionInCount() {
        return transactionInCount;
    }

    public TxIn[] getTxIns() {
        return txIns;
    }

    public VariableInteger getTransactionOutCount() {
        return transactionOutCount;
    }

    public TxOut[] getTxOuts() {
        return txOuts;
    }

    public UnsignedInteger getLockTime() {
        return lockTime;
    }

    public String getHash(CryptoCurrency coin) {
        byte[] doubleHashedResult = CryptoUtil.doubleHash(toByteArray(), coin.getEncryptionMethod());

        return PrimitiveUtil.byteArrayToString(doubleHashedResult);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
