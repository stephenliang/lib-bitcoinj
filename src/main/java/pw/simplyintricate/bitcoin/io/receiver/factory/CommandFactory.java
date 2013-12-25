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

package pw.simplyintricate.bitcoin.io.receiver.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import pw.simplyintricate.bitcoin.io.receiver.handler.CommandHandler;
import pw.simplyintricate.bitcoin.models.Command;

import java.util.HashMap;

@Configuration
public class CommandFactory {
    @Autowired private HashMap<Command, CommandHandler> commandHandlerHashMap;

    public CommandHandler getCommandHandler(byte[] command) {
        if (command.length > 12 || command.length == 0) {
            throw new IllegalArgumentException("The command is of invalid length. " +
                    "It must be less than or equal to 12 or greater than zero. It is " + command.length);
        }

        Command protocolCommand = Command.valueOf(new String(command).trim());

        CommandHandler commandHandler = commandHandlerHashMap.get(protocolCommand);

        if (commandHandler == null) {
            throw new IllegalArgumentException("The input command has no associating handler! " +
                    "Command: "+ protocolCommand.name());
        }

        return commandHandler;
    }
}
