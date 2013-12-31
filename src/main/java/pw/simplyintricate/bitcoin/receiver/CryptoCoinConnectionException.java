package pw.simplyintricate.bitcoin.receiver;

/**
 * An exception while trying to connect or receive from a node
 */
public class CryptoCoinConnectionException extends RuntimeException {
    public CryptoCoinConnectionException(String message, Throwable e) {
        super(message, e);
    }
}
