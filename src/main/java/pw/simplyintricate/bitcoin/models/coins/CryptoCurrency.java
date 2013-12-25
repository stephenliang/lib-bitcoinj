package pw.simplyintricate.bitcoin.models.coins;

/**
 * Created by Stephen on 12/24/13.
 */
public enum CryptoCurrency {
    BITCOIN(BitcoinConstants.ENCRYPTION_METHOD, BitcoinConstants.MAGIC_HEADER, BitcoinConstants.PROTOCOL_VERSION);

    private final String encryptionMethod;
    private final byte[] magicHeader;
    private final int protocolVersion;

    CryptoCurrency(String encryptionMethod, byte[] magicHeader, int protocolVersion) {
        if (magicHeader.length != 4) {
            throw new IllegalArgumentException("The magic header must be of length 4!");
        }

        this.encryptionMethod = encryptionMethod;
        this.magicHeader = magicHeader;
        this.protocolVersion = protocolVersion;
    }

    public String getEncryptionMethod() {
        return encryptionMethod;
    }

    public byte[] getMagicHeader() {
        return magicHeader;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }
}
