package pw.simplyintricate.bitcoin.receiver;

import java.io.IOException;

/**
 * Created by Stephen on 12/22/13.
 */
public class CryptoCoinConnectionException extends RuntimeException {
    public CryptoCoinConnectionException(String message, IOException e) {
        super(message, e);
    }
}
