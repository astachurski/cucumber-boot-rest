package intj.ghchecker3.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;


import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.*;
import intj.ghchecker3.domain.TrackingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple example of how to access the Google Analytics API using a service
 * account.
 */

@Service
public class GaAccountInspectorService {
    private static final String APPLICATION_NAME = "psychic-empire-199114";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String KEY_FILE_LOCATION = "/home/adrian/Development/SDA/Spring/gh-checker-3/src/main/resources/client_secrets.json";
    //private static final String KEY_FILE_LOCATION = "/home/adrian/Development/SDA/Spring/gh-checker-3/src/main/resources/client_secrets-2.json";

    private static Logger logger = LoggerFactory.getLogger("GaAccountInspectorService");

    private Analytics analytics;

    @Autowired
    private TrackingGHAccountsService trackingGHAccountsService;

    public static void main(String[] args) throws IOException {
        GaAccountInspectorService gaAccountInspectorService = new GaAccountInspectorService();
        gaAccountInspectorService.getTrackinEntityReport(1);
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

    public List<TrackingEntity> getTrackinEntityReport(Integer limit) throws IOException {

        List<TrackingEntity> trackingEntitiesReports = new ArrayList<>();

        // Query for the list of all accounts associated with the service account.
        Accounts accounts = analytics.management().accounts().list().execute();

        Long counter = 0L;

        // ----------- traversing GA accounts
        for (Account account : accounts.getItems()) {

            counter++;

            logger.info("acquired from GA data on : " +
                    account.getId() + "/" + account.getName() + "/" + account.getKind());

            // Query for the list of properties associated with the account.
            Webproperties properties = analytics.management().webproperties()
                    .list(account.getId()).execute();

            // ------------- traversing properties/applications
            for (Webproperty webproperty : properties.getItems()) {

                logger.info("acuired data on property: " + webproperty.getId() + "/" + webproperty.getWebsiteUrl());

                //tracking entity is the smallest tracking unit - that's why it is created in context of
                // webproprety (GA property).
                TrackingEntity trackingEntity =
                        new TrackingEntity(0L,
                                account.getName(),
                                webproperty.getName(),
                                webproperty.getId(),
                                webproperty.getWebsiteUrl());

                Boolean isGhTrackingEntity = trackingGHAccountsService.isTrackingEntityGrowthHouse(trackingEntity);
                logger.info(" is tracking entity GrowthHouse ? : " + isGhTrackingEntity);

                trackingEntity.setGrowthHouse(isGhTrackingEntity);
                trackingEntity.setAccountNumber(account.getId());

                Profiles profiles = analytics.management().profiles()
                        .list(account.getId(), webproperty.getId()).execute();

                if (profiles.getItems().size() > 1)
                    logger.info(" This account has MORE THAN DEFAULT view!");


                // traversing views/profiles for property/application

                StringBuilder sb = new StringBuilder();
                boolean isFirst = true; // we gather analytics data from FIRST view only, but names from all
                for (Profile profile : profiles.getItems()) {
                    sb.append(profile.getName() + ",");
                    if (isFirst) {
                        logger.info("VIEW found: " + profile.getName());
                        Map<String, String> profileReports = getProfileReports(profile, analytics);

                        String sessions7daysAgo = profileReports.get("Sessions7daysAgo");
                        String bounces7daysAgo = profileReports.get("Bounces7daysAgo");
                        String bounceRate7daysAgo = profileReports.get("BounceRate7daysAgo");
                        String pageviewsPerSession7daysAgo = profileReports.get("PageviewsPerSession7daysAgo");

                        trackingEntity.setSessionsLast7days(sessions7daysAgo);
                        trackingEntity.setBouncesLast7days(bounces7daysAgo);
                        trackingEntity.setBounceRateLast7days(bounceRate7daysAgo);
                        trackingEntity.setPageviewsPerSessionLast7days(pageviewsPerSession7daysAgo);

                        isFirst = false;
                    } else {
                        logger.info("aborting metric collection - there are more views, acquired data for first!");
                    }

                }

                trackingEntity.setViewNames(sb.toString());
                trackingEntitiesReports.add(trackingEntity);
            }


            if ((counter >= limit) && (limit > 0))
                break;
        }
        return trackingEntitiesReports;
    }

    private Map<String, String> getProfileReports(Profile profile, Analytics analytics) {
        Map<String, String> resultReports = new HashMap<>();

        try {

            String profileId = profile.getId();

            GaData gaSessionsData = getGaSessions(analytics, profileId);
            GaData gaBouncesData = getGaBounces(analytics, profileId);
            GaData gaBounceRateData = getGaBounceRate(analytics, profileId);
            GaData gaPageviewsPerSessionData = getGaPageViewsPerSession(analytics, profileId);

            String metricName = "Sessions7daysAgo";
            resultReports.put(metricName, formatGaResults(gaSessionsData, metricName));
            metricName = "Bounces7daysAgo";
            resultReports.put(metricName, formatGaResults(gaBouncesData, metricName));
            metricName = "BounceRate7daysAgo";
            resultReports.put(metricName, formatGaResults(gaBounceRateData, metricName));
            metricName = "PageviewsPerSession7daysAgo";
            resultReports.put(metricName, formatGaResults(gaPageviewsPerSessionData, metricName));


        } catch (IOException e) {
            logger.info(" could not obtain GA metrics for view  " + profile.getName());
        }

        return resultReports;
    }

    private GaData getGaSessions(Analytics analytics, String profileId) throws IOException {
        // Query the Core Reporting API for the number of sessions
        // in the past seven days.
        return analytics.data().ga()
                .get("ga:" + profileId, "7daysAgo", "yesterday", "ga:sessions")
                .execute();
    }

    private GaData getGaBounces(Analytics analytics, String profileId) throws IOException {
        return analytics.data().ga()
                .get("ga:" + profileId, "7daysAgo", "yesterday", "ga:bounces")
                .execute();
    }

    private GaData getGaBounceRate(Analytics analytics, String profileId) throws IOException {
        return analytics.data().ga()
                .get("ga:" + profileId, "7daysAgo", "yesterday", "ga:bounceRate")
                .execute();
    }

    private GaData getGaPageViewsPerSession(Analytics analytics, String profileId) throws IOException {
        return analytics.data().ga()
                .get("ga:" + profileId, "7daysAgo", "yesterday", "ga:pageviewsPerSession")
                .execute();
    }





    private String formatGaResults(GaData results, String metricName) {
        // Parse the response from the Core Reporting API for
        // the profile name and number of sessions.
        String result = "";
        if (results != null && results.getRows() != null) {
            result = results.getRows().get(0).get(0);
        } else {
            logger.info("No results found for: " + metricName);
        }

        return result;
    }
}