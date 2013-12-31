/*
 * lib-bitcoin
 * Copyright (c) 2014, Stephen Liang, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 */

package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.primitives.UnsignedInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.util.PrimitiveUtil;

import java.io.UnsupportedEncodingException;

public class Message {
    private byte[] command; //char[12]
    private UnsignedInteger length; //unit32
    private byte[] checksum; //uint32
    private byte[] payload; //var
    private CryptoCurrency cryptoCurrency;

    private Message() {}

    public byte[] toByteArray() {
        if (payload.length == 0) {
            throw new IllegalStateException("The payload is empty, did you forget to initialize the message?");
        }

        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.write(cryptoCurrency.getMagicHeader());
        writer.write(command);
        writer.writeInt(length.intValue());
        writer.write(checksum);
        writer.write(payload);

        return writer.toByteArray();
    }

    public static class Builder {
        private Message message;

        public Builder() {
            message = new Message();
        }

        public Builder setCommand(String command) {
            try {
                message.command = PrimitiveUtil.convertToNullTerminatedString(command);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException(e);
            }

            return this;
        }

        public Builder setLength(UnsignedInteger length) {
            message.length = length;
            return this;
        }

        public Builder setChecksum(byte[] checksum) {
            message.checksum = checksum;
            return this;
        }

        public Builder setPayload(byte[] payload) {
            message.payload = payload;
            return this;
        }

        public Builder setCryptoCurrency(CryptoCurrency cryptoCurrency) {
            message.cryptoCurrency = cryptoCurrency;
            return this;
        }

        public Message build() {
            return message;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
