package intj.ghchecker3;


//something that is meant to TRACK (as sink) javascript datasources (snippets with UA-codes on remote hosts)
public class TrackingEntity {
    private Long id;
    private String accountName;
    private String propertyName;
    private String trackingId;

    @java.beans.ConstructorProperties({"id", "accountName", "propertyName", "trackingId"})
    TrackingEntity(Long id, String accountName, String propertyName, String trackingId) {
        this.id = id;
        this.accountName = accountName;
        this.propertyName = propertyName;
        this.trackingId = trackingId;
    }

    public static TrackingEntityBuilder builder() {
        return new TrackingEntityBuilder();
    }

    public static class TrackingEntityBuilder {
        private Long id;
        private String accountOwner;
        private String propertyName;
        private String trackingId;

        TrackingEntityBuilder() {
        }

        public TrackingEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TrackingEntityBuilder accountOwner(String accountOwner) {
            this.accountOwner = accountOwner;
            return this;
        }

        public TrackingEntityBuilder propertyName(String propertyName) {
            this.propertyName = propertyName;
            return this;
        }

        public TrackingEntityBuilder trackingId(String trackingId) {
            this.trackingId = trackingId;
            return this;
        }

        public TrackingEntity build() {
            return new TrackingEntity(id, accountOwner, propertyName, trackingId);
        }

        public String toString() {
            return "TrackingEntity.TrackingEntityBuilder(id=" + this.id + ", accountName=" + this.accountOwner + ", propertyName=" + this.propertyName + ", trackingId=" + this.trackingId + ")";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    @Override
    public String toString() {
        return String.format("\nAccount Name: %s \nProperty Name: %s \nTracking Code: %s \n",
                accountName, propertyName, trackingId);
    }
}
