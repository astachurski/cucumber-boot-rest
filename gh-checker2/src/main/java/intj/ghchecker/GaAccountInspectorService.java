package intj.ghchecker;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.*;
import org.springframework.stereotype.Service;


import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;


/**
 * A simple example of how to access the Google Analytics API using a service
 * account.
 */

@Service
public class GaAccountInspectorService {
    private static final String APPLICATION_NAME = "psychic-empire-199114";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String KEY_FILE_LOCATION = "/home/adrian/Development/SDA/Spring/gh-checker2/src/main/resources/client_secrets.json";

    public static void main(String[] args) {
        try {
            Analytics analytics = initializeAnalytics();

            System.out.println(analytics.management().accounts().list());

            String profile = getAccountOverview(analytics);

            System.out.println(profile);
          //  System.out.println("First Profile Id: " + profile);
          //  printResults(getGaResults(analytics, profile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Analytics initializeAnalytics() throws GeneralSecurityException, IOException {

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream(KEY_FILE_LOCATION))
                .createScoped(AnalyticsScopes.all());

        // Construct the Analytics service object.
        return new Analytics.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    private static String getAccountOverview(Analytics analytics) throws IOException {

        // Query for the list of all accounts associated with the service account.
        Accounts accounts = analytics.management().accounts().list().execute();

        for (Account account: accounts.getItems()){
            System.out.println("account id: " + account.getId() + "  -  " + account.getName());

            // Query for the list of properties associated with the first account.
            Webproperties properties = analytics.management().webproperties()
                    .list(account.getId()).execute();

            for (Webproperty webproperty: properties.getItems()){

                System.out.println("website: " + webproperty.getWebsiteUrl());


                // Query for the list views (profiles) associated with the property.
                Profiles profiles = analytics.management().profiles()
                        .list(account.getId(), webproperty.getId()).execute();

                for (Profile profile: profiles.getItems()){
                    // Return the first (view) profile associated with the property.
                    String profileId = profile.getId();
                    System.out.println("  view id: " + profileId);


                    GaData gaData = getGaResults(analytics, profileId);

                    printResults(gaData);
                    System.out.println("");
                }
            }

            System.out.println("");
        }
        return "";
    }

    private static GaData getGaResults(Analytics analytics, String profileId) throws IOException {
        // Query the Core Reporting API for the number of sessions
        // in the past seven days.
        return analytics.data().ga()
                .get("ga:" + profileId, "7daysAgo", "today", "ga:sessions")
                .execute();
    }

    private static void printResults(GaData results) {
        // Parse the response from the Core Reporting API for
        // the profile name and number of sessions.
        if (results != null && results.getRows() != null) {
            System.out.println("View (Profile) Name: "
                    + results.getProfileInfo().getProfileName());
            System.out.println("Total Sessions: " + results.getRows().get(0).get(0));
        } else {
            System.out.println("No results found");
        }
    }
}