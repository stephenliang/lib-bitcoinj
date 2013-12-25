package pw.simplyintricate.bitcoin.models;

/**
 * Created by Stephen on 12/24/13.
 */
public enum Command {
    version("version"),
    verack("verack"),
    inv("inv"),
    addr("addr"),
    getdata("getdata");

    private final String protocolCommandString;

    Command(String protocolCommandString) {
        this.protocolCommandString = protocolCommandString;
    }

    public String getProtocolCommandString() {
        return protocolCommandString;
    }
}
