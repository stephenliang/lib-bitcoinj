/*
 * lib-bitcoin
 * Copyright (c) 2014, Stephen Liang, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package pw.simplyintricate.bitcoin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pw.simplyintricate.bitcoin.io.receiver.factory.CommandFactory;
import pw.simplyintricate.bitcoin.io.receiver.handler.CommandHandler;
import pw.simplyintricate.bitcoin.io.receiver.handler.impl.AddrHandlerImpl;
import pw.simplyintricate.bitcoin.io.receiver.handler.impl.InventoryHandlerImpl;
import pw.simplyintricate.bitcoin.io.receiver.handler.impl.VersionAcknowledgementHandlerImpl;
import pw.simplyintricate.bitcoin.io.receiver.handler.impl.VersionHandlerImpl;
import pw.simplyintricate.bitcoin.models.Command;

import java.util.HashMap;

/**
 * Spring Application context master configuration
 */
@Configuration
@SuppressWarnings("unused")
public class AppConfig {
    public @Bean VersionAcknowledgementHandlerImpl versionAcknowledgementHandler() {
        return new VersionAcknowledgementHandlerImpl();
    }

    public @Bean VersionHandlerImpl versionHandler() {
        return new VersionHandlerImpl();
    }

    public @Bean InventoryHandlerImpl inventoryHandler() {
        return new InventoryHandlerImpl();
    }

    public @Bean AddrHandlerImpl addrHandler() {
        return new AddrHandlerImpl();
    }

    public @Bean HashMap<Command, CommandHandler> commandHandlerHashMap() {
        HashMap<Command, CommandHandler> commandHandlerHashMap = new HashMap<>();

        commandHandlerHashMap.put(Command.verack, versionAcknowledgementHandler());
        commandHandlerHashMap.put(Command.version, versionHandler());
        commandHandlerHashMap.put(Command.inv, inventoryHandler());
        commandHandlerHashMap.put(Command.addr, addrHandler());

        return commandHandlerHashMap;
    }

    public @Bean CommandFactory commandFactory() {
        return new CommandFactory();
    }
}
