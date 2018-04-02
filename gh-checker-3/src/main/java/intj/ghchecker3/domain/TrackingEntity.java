package intj.ghchecker3.domain;


//something that is meant to TRACK (as sink) javascript datasources (snippets with UA-codes on remote hosts)


import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class TrackingEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String accountName;
    private String accountNumber;
    private String propertyName;
    private String trackingId;
    private Boolean growthHouse = false;
    private String website;
    private String viewNames;

    private String websiteActualCodes; // from crawling/parsing!

    //metrics read from GA for comparisons across accounts and with GH
    private String sessionsLast7days;
    private String bouncesLast7days;
    private String bounceRateLast7days;
    private String pageviewsPerSessionLast7days;

    @Transient
    private List<String> websiteActualCodesList = new ArrayList<>();
    private Integer sendingGAcodes;// from crawling/parsing!

/*
    @OneToMany
    private List<WebsiteExtractionData> websiteExtractionDataList = new ArrayList<>();
*/

    public TrackingEntity(Long id, String accountName, String propertyName, String trackingId, String website) {
        this.id = id;
        this.accountName = accountName;
        this.propertyName = propertyName;
        this.trackingId = trackingId;
        this.website = website;
    }

    public TrackingEntity() {


    }

    public String getBounceRateLast7days() {
        return bounceRateLast7days;
    }

    public void setBounceRateLast7days(String bounceRateLast7days) {
        this.bounceRateLast7days = bounceRateLast7days;
    }

    public String getPageviewsPerSessionLast7days() {
        return pageviewsPerSessionLast7days;
    }

    public void setPageviewsPerSessionLast7days(String pageviewsPerSessionLast7days) {
        this.pageviewsPerSessionLast7days = pageviewsPerSessionLast7days;
    }

    public String getSessionsLast7days() {
        return sessionsLast7days;
    }

    public void setSessionsLast7days(String sessionsLast7days) {
        this.sessionsLast7days = sessionsLast7days;
    }

    public String getBouncesLast7days() {
        return bouncesLast7days;
    }

    public void setBouncesLast7days(String bouncesLast7days) {
        this.bouncesLast7days = bouncesLast7days;
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

    public void addWebsiteActualCodes(List<String> codes) {
        this.websiteActualCodesList.addAll(codes);
    }

    public List<String> getWebsiteActualCodesList() {
        String[] split = this.websiteActualCodes.split(",");

        List<String> collect = Lists.newArrayList(split).stream().filter(x -> !x.isEmpty()).collect(Collectors.toList());
        this.websiteActualCodesList.addAll(collect);
       /* if (split.length > 0){
            for (String s : split) {
                this.websiteActualCodesList.add(s);
            }
        }*/
        return websiteActualCodesList;
    }

    public void setViewNames(String profiles) {
        this.viewNames = profiles;
    }

    public String getViewNames() {
        return viewNames;
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
