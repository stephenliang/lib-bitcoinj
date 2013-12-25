package pw.simplyintricate.bitcoin.io.sender;

/**
 * Created by Stephen on 12/27/13.
 */
public class SenderException extends RuntimeException {
    public SenderException(String message, Throwable e) {
        super(message, e);
    }
}
