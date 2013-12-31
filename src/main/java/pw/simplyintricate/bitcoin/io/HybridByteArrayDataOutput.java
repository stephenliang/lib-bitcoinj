/*
 * lib-bitcoinj
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
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 */

package pw.simplyintricate.bitcoin.io;

import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataOutput;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Implementation of a byte array data output that writes in little endian for ints and longs but big endian for
 * others
 */
public class HybridByteArrayDataOutput implements ByteArrayDataOutput {
    final DataOutput output;
    final ByteArrayOutputStream byteArrayOutputSteam;

    public HybridByteArrayDataOutput() {
        this.byteArrayOutputSteam = new ByteArrayOutputStream();
        this.output = new DataOutputStream(byteArrayOutputSteam);
    }

    @Override
    public void write(int b) {
      try {
        output.write(b);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void write(byte[] b) {
      try {
        output.write(b);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void write(byte[] b, int off, int len) {
      try {
        output.write(b, off, len);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeBoolean(boolean v) {
      try {
        output.writeBoolean(v);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeByte(int v) {

      try {
        output.writeByte(v);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeShort(int v) {

      try {
        output.writeShort(EndianUtils.swapShort((short) v));
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeChar(int v) {

      try {
        output.writeChar(v);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeInt(int v) {

      try {
        output.writeInt(EndianUtils.swapInteger(v));
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeLong(long v) {

      try {
        output.writeLong(EndianUtils.swapLong(v));
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeFloat(float v) {

      try {
        output.writeFloat(v);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeDouble(double v) {

      try {
        output.writeDouble(v);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeChars(String s) {

      try {
        output.writeChars(s);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public void writeUTF(String s) {

      try {
        output.writeUTF(s);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    @Deprecated
    public void writeBytes(String s) {

      try {
        output.write(s.getBytes(Charsets.UTF_8));
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
    }

    @Override
    public byte[] toByteArray() {
        return byteArrayOutputSteam.toByteArray();
    }
}
