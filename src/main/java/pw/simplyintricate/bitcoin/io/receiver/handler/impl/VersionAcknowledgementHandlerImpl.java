package pw.simplyintricate.bitcoin.io.receiver.handler.impl;

import pw.simplyintricate.bitcoin.io.receiver.handler.CommandHandler;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.models.datastructures.VersionAcknowledgement;

import java.io.DataInputStream;

/**
 * Created by Stephen on 12/27/13.
 */
public class VersionAcknowledgementHandlerImpl implements CommandHandler {
    @Override
    public void processBytes(DataInputStream reader, CryptoCurrency coin) {
        VersionAcknowledgement versionAcknowledgement = new VersionAcknowledgement.Builder()
                .build();

        System.out.println("Acknowledged the version command!");
    }
}
