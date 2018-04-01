package intj.ghchecker3.domain;

import javax.persistence.*;


public class WebsiteExtractionData {
    @Id
    @GeneratedValue
    private Long id;

    private String uaCode;

    //@JoinColumn(name = )
    @ManyToOne
    private TrackingEntity trackingEntity;

    public WebsiteExtractionData() {

    }

    public String getUaCode() {
        return uaCode;
    }

    public void setUaCode(String uaCode) {
        this.uaCode = uaCode;
    }
}
