package pw.simplyintricate.bitcoin.io.receiver.handler.impl;

import com.google.common.io.LittleEndianDataInputStream;
import pw.simplyintricate.bitcoin.io.receiver.handler.CommandHandler;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;
import pw.simplyintricate.bitcoin.models.datastructures.BlockVersion;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by Stephen on 12/27/13.
 */
public class VersionHandlerImpl implements CommandHandler {
    @Override
    public void processBytes(DataInputStream dataInputStream, CryptoCurrency coin) {
        try {
            LittleEndianDataInputStream reader = new LittleEndianDataInputStream(dataInputStream);

            BlockVersion blockVersion = BlockVersion.fromInputStream(reader);

            System.out.println("Received a version command, got: " + blockVersion);

        } catch(IOException e) {
            throw new IllegalArgumentException("There was an error while parsing the payload", e);
        }
    }
}
