package pw.simplyintricate.bitcoin.models.datastructures;

/**
 * Created by Stephen on 12/27/13.
 */
public class VersionAcknowledgement {
    public static class Builder {
        private VersionAcknowledgement versionAcknowledgement;

        public Builder() {
            versionAcknowledgement = new VersionAcknowledgement();
        }

        public VersionAcknowledgement build() {
            return versionAcknowledgement;
        }
    }
}
