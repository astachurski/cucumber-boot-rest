package intj.ghchecker3.domain;

//representatino of a client as known to GrowthHouse - stored in periodically refreshd DynamoDB SugarTable

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ClientGHSugar {

    @Id
    @GeneratedValue
    private Long id;

    private String clientId;
    private String advisorId;
    private String name;
    private String organizationNumber;
    private String website;

    public ClientGHSugar(String clientId, String name, String organizationNumber, String website, String advisorId) {
        this.clientId = clientId;
        this.name = name;
        this.organizationNumber = organizationNumber;
        this.website = website;
        this.advisorId = advisorId;
    }

    public ClientGHSugar() {

    }

    public void setAdvisorId(String advisorId) {
        this.advisorId = advisorId;
    }

    public String getAdvisorId() {
        return advisorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return String.format("name:%s, website:%s", name, website);
    }
}
