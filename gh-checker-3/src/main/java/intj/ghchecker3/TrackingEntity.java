package intj.ghchecker3;


//something that is meant to TRACK (as sink) javascript datasources (snippets with UA-codes on remote hosts)


public class TrackingEntity {
    private Long id;
    private String accountName;
    private String accountNumber;
    private String propertyName;
    private String trackingId;
    private Boolean growthHouse = false;
    private String website;
    private String profiles;
    private String websiteActualCodes; // from crawling/parsing!
    private Integer sendingGAcodes;// from crawling/parsing!

    public TrackingEntity(Long id, String accountName, String propertyName, String trackingId, String website) {
        this.id = id;
        this.accountName = accountName;
        this.propertyName = propertyName;
        this.trackingId = trackingId;
        this.website = website;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setSendingGAcodes(Integer sendingGAcodes) {
        this.sendingGAcodes = sendingGAcodes;
    }

    public Integer getSendingGAcodes() {
        return sendingGAcodes;
    }

    public void setWebsiteActualCodes(String websiteActualCodes) {
        this.websiteActualCodes = websiteActualCodes;
    }

    public String getWebsiteActualCodes() {
        return websiteActualCodes;
    }

    public void setProfiles(String profiles) {
        this.profiles = profiles;
    }

    public String getProfiles() {
        return profiles;
    }

    public void setGrowthHouse(Boolean growthHouse) {
        this.growthHouse = growthHouse;
    }

    public Boolean getGrowthHouse() {
        return growthHouse;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
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
