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

package pw.simplyintricate.bitcoin.models;

/**
 * Command enums useful for determining how to interact with the protocol
 */
public enum Command {
    version("version"),
    verack("verack"),
    inv("inv"),
    addr("addr"),
    getdata("getdata"),
    getaddr("getaddr");

    private final String protocolCommandString;

    Command(String protocolCommandString) {
        this.protocolCommandString = protocolCommandString;
    }

    public String getProtocolCommandString() {
        return protocolCommandString;
    }
}
