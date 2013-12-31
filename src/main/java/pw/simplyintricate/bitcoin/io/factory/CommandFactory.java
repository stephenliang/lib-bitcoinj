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

package pw.simplyintricate.bitcoin.io.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import pw.simplyintricate.bitcoin.io.handler.CommandHandler;
import pw.simplyintricate.bitcoin.models.Command;

import java.util.HashMap;

/**
 * A command factory provides a mapping of a command to its handler
 */
@Configuration
public class CommandFactory {
    @Autowired @SuppressWarnings("unused")
    private HashMap<Command, CommandHandler> commandHandlerHashMap;

    /**
     * Gets the command handler for the given input command
     * @param command The command from input stream in the form of a byte array. This must be of length 12 or less
     * @return The CommandHelper for the command
     * @throws java.lang.IllegalArgumentException If there is no handler to handle the command or the command is of
     * length 12 or less
     */
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
