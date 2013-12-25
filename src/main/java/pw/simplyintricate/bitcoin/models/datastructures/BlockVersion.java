package pw.simplyintricate.bitcoin.models.datastructures;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import pw.simplyintricate.bitcoin.io.HybridByteArrayDataOutput;

import java.io.IOException;

/**
 * Created by Stephen on 12/23/13.
 */
public final class BlockVersion {
    private final int version; //int32
    private final UnsignedInteger services; //unsigned int64
    private final Long timestamp; //int64
    private final NetworkAddress receivingNodeNetworkAddress;
    private final NetworkAddress emittingNodeNetworkAddress;
    private final byte[] nonce;
    private final VariableString userAgent;
    private final int startingHeight;

    public BlockVersion(int version,
                         UnsignedInteger services,
                         Long timestamp,
                         NetworkAddress receivingNodeNetworkAddress,
                         NetworkAddress emittingNodeNetworkAddress,
                         byte[] nonce,
                         VariableString userAgent,
                         int startingHeight) {
        this.version = version;
        this.services = services;
        this.timestamp = timestamp;
        this.receivingNodeNetworkAddress = receivingNodeNetworkAddress;
        this.emittingNodeNetworkAddress = emittingNodeNetworkAddress;
        this.nonce = nonce;
        this.userAgent = userAgent;
        this.startingHeight = startingHeight;
    }

    public byte[] toByteArray() {
        HybridByteArrayDataOutput writer = new HybridByteArrayDataOutput();

        writer.writeInt(version);
        writer.writeLong(services.longValue());
        writer.writeLong(timestamp);
        writer.write(receivingNodeNetworkAddress.toByteArray());
        writer.write(emittingNodeNetworkAddress.toByteArray());
        writer.write(nonce);
        writer.write(userAgent.toByteArray());
        writer.writeInt(startingHeight);

        return writer.toByteArray();
    }

    public static BlockVersion fromInputStream(LittleEndianDataInputStream reader) throws IOException {
        int version = reader.readInt();
        long services = reader.readLong();
        long timestamp = reader.readLong();
        NetworkAddress receivingNetworkAddress = NetworkAddress.fromInputStream(reader);
        NetworkAddress emittingNetworkAddress = NetworkAddress.fromInputStream(reader);
        byte[] nonce = new byte[8];
        reader.read(nonce, 0, nonce.length);
        VariableString userAgent = VariableString.fromInputStream(reader);
        int startingHeight = reader.readInt();

        BlockVersion blockVersion = new BlockVersion(version, UnsignedInteger.valueOf(services), timestamp,
                receivingNetworkAddress, emittingNetworkAddress, nonce, userAgent, startingHeight);

        return blockVersion;
    }
}
