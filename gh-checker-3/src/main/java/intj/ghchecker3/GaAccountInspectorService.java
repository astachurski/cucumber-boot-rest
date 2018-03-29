package intj.ghchecker3;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;


import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple example of how to access the Google Analytics API using a service
 * account.
 */

@Service
public class GaAccountInspectorService {
    private static final String APPLICATION_NAME = "psychic-empire-199114";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String KEY_FILE_LOCATION = "/home/adrian/Development/SDA/Spring/gh-checker-3/src/main/resources/client_secrets.json";

    private Analytics analytics;

    public static void main(String[] args) throws IOException {
        GaAccountInspectorService gaAccountInspectorService = new GaAccountInspectorService();
        gaAccountInspectorService.getAccountsReports();
    }

    @PostConstruct
    public void init() {
        try {
            analytics = initializeAnalytics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Analytics initializeAnalytics() throws GeneralSecurityException, IOException {

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream(KEY_FILE_LOCATION))
                .createScoped(AnalyticsScopes.all());
        // Construct the Analytics service object.
        return new Analytics.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    public List<AccountReportItem> getAccountsReports() throws IOException {


        List<AccountReportItem> reports = new ArrayList<>();

        // Query for the list of all accounts associated with the service account.
        Accounts accounts = analytics.management().accounts().list().execute();

        // ----------- traversing GA accounts
        for (Account account : accounts.getItems()) {

            AccountReportItem accountReportItem = new AccountReportItem(account.getId(), account.getName());

            //System.out.println("account id: " + account.getId() + "  -  " + account.getName());

/*
            // Query for the list of properties associated with the account.
            Webproperties properties = analytics.management().webproperties()
                    .list(account.getId()).execute();

            // traversing properties/applications
            for (Webproperty webproperty : properties.getItems()) {

                Profiles profiles = analytics.management().profiles()
                        .list(account.getId(), webproperty.getId()).execute();

                // traversing views/profiles for property/application
                for (Profile profile : profiles.getItems()) {
                    getProfileReports(profile, analytics);
                }
            }
*/

            reports.add(accountReportItem);
        }
        return reports;
    }

    private List<String> getPropertyReport(Webproperty webproperty, Analytics analytics) {
        List<String> resultReports = new ArrayList<>();

        System.out.println("website reported: " + webproperty.getWebsiteUrl());

        return resultReports;

    }

    private List<String> getProfileReports(Profile profile, Analytics analytics) {

        List<String> resultReports = new ArrayList<>();

        try {

            // Return the first (view) profile associated with the property.
            String profileId = profile.getId();
            System.out.println("  view id: " + profileId);

            GaData gaSessionsData = getGaSessions(analytics, profileId);
            GaData gaBouncesData = getGaBounces(analytics, profileId);

            resultReports.add(formatGaResults(gaSessionsData, "Sessions7daysAgo"));
            resultReports.add(formatGaResults(gaBouncesData, "Bounces7daysAgo"));

            System.out.println("------------------------------------");
        } catch (IOException e) {
            System.out.printf("----- IO exception !!! -----");
        }

        return resultReports;

    }


    private GaData getGaSessions(Analytics analytics, String profileId) throws IOException {
        // Query the Core Reporting API for the number of sessions
        // in the past seven days.
        return analytics.data().ga()
                .get("ga:" + profileId, "7daysAgo", "today", "ga:sessions")
                .execute();
    }

    private GaData getGaBounces(Analytics analytics, String profileId) throws IOException {
        return analytics.data().ga()
                .get("ga:" + profileId, "7daysAgo", "today", "ga:bounces")
                .execute();
    }

    private String formatGaResults(GaData results, String metricName) {
        // Parse the response from the Core Reporting API for
        // the profile name and number of sessions.
        String result = "";
        if (results != null && results.getRows() != null) {
            result = metricName + " : " + results.getRows().get(0).get(0);
        } else {
            System.out.println("No results found for: " + metricName);
        }

        return result;
    }
}