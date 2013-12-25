package pw.simplyintricate.bitcoin.io.receiver.handler;

import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;

import java.io.DataInputStream;

/**
 * Created by Stephen on 12/27/13.
 */
public interface CommandHandler {
    public void processBytes(DataInputStream reader, CryptoCurrency coin);

}
