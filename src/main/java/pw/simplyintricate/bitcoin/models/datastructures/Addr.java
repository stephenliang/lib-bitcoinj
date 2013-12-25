package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;

/**
 * Created by Stephen on 12/28/13.
 */
public class Addr {
    private final VariableInteger count;
    private final NetworkAddress[] networkAddresses;

    public VariableInteger getCount() {
        return count;
    }

    public NetworkAddress[] getNetworkAddresses() {
        return networkAddresses;
    }

    public Addr(VariableInteger count, NetworkAddress[] networkAddresses) {
        this.count = count;
        this.networkAddresses = networkAddresses;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();
        writer.write(count.toByteArray());
        int intLength = count.getValue().intValue();

        for (int i = 0; i < intLength; i++) {
            writer.write(networkAddresses[i].toByteArray());
        }

        return writer.toByteArray();
    }

    public static Addr fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        VariableInteger length = VariableInteger.fromInputStream(reader);
        int intLength = length.getValue().intValue();
        NetworkAddress[] networkAddresses = new NetworkAddress[intLength];

        for (int i = 0; i < intLength; i++) {
            networkAddresses[i] = NetworkAddress.fromInputStream(reader, true);
        }

        return new Addr(length, networkAddresses);
    }
}
