package pw.simplyintricate.bitcoin.receiver;

import org.junit.Before;
import org.junit.Test;
import pw.simplyintricate.bitcoin.io.receiver.factory.CommandFactory;
import pw.simplyintricate.bitcoin.io.receiver.handler.CommandHandler;
import pw.simplyintricate.bitcoin.io.receiver.handler.impl.VersionAcknowledgementHandlerImpl;
import pw.simplyintricate.bitcoin.models.Command;
import pw.simplyintricate.bitcoin.models.coins.CryptoCurrency;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by Stephen on 12/23/13.
 */
public class CryptoCoinReceiverTest {
    private CryptoCoinReceiver cryptoCoinReceiver;
    private CommandFactory commandFactory;

    @Before
    public void setUp() {
        HashMap<Command, CommandHandler> handlerHashMap = new HashMap<Command, CommandHandler>();
        handlerHashMap.put(Command.version, new VersionAcknowledgementHandlerImpl());
        commandFactory = new CommandFactory();
        cryptoCoinReceiver = new CryptoCoinReceiver("72.233.92.90", 8333, CryptoCurrency.BITCOIN, commandFactory);
    }

    @Test
    public void testSendVersion() {
        cryptoCoinReceiver.sendVersion();
    }

    @Test
    public void testEncrypt() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] input = "".getBytes();
            //[93,-10,-32,-30,118,19,89,-45,10,-126,117,5,-114,41,-97,-52,3,-127,83,69,69,-11,92,-12,62,65,-104,63,93,76,-108,86]
            byte[] input2 = {-29,-80,-60,66,-104,-4,28,20,-102,-5,-12,-56,-103,111,-71,36,39,-82,65,-28,100,-101,-109,76,-92,-107,-103,27,120,82,-72,85};
            byte[] firstRound = messageDigest.digest(input2);
            byte[] secondRound = messageDigest.digest(firstRound);

            System.out.println(secondRound);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
