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

package pw.simplyintricate.bitcoin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import pw.simplyintricate.bitcoin.config.AppConfig;
import pw.simplyintricate.bitcoin.io.receiver.factory.CommandFactory;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.receiver.CryptoCoinReceiver;

/**
 * Starts up the application and connects to the default node
 */
@ComponentScan
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        CommandFactory commandFactory = context.getBean(CommandFactory.class);

        CryptoCoinReceiver cryptoCoinReceiver =
                new CryptoCoinReceiver("72.233.92.90", 8333, CryptoCurrency.BITCOIN, commandFactory);

        cryptoCoinReceiver.sendVersion();
    }
}
