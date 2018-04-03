package intj.ghchecker3;

import ch.qos.logback.core.net.server.Client;
import com.google.api.client.util.Lists;
import intj.ghchecker3.domain.ClientGHSugar;
import intj.ghchecker3.domain.SiteExtractionReport;
import intj.ghchecker3.domain.TrackingEntity;
import intj.ghchecker3.persistence.ClientGHSugarRepository;
import intj.ghchecker3.persistence.TrackingEntityRepository;
import intj.ghchecker3.services.*;
import intj.ghchecker3.tools.HttpReformatter;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger("MainController");

    @Autowired
    private DynamoDBclientDataExtractor dynamoDBclientDataExtractor;

    @Autowired
    private TrackingGHAccountsService trackingGHAccountsService;

    @Autowired
    private HostNameService hostNameService;

    @Autowired
    private SiteMetadataExtractor siteMetadataExtractor;

    @Autowired
    private GaAccountInspectorService gaAccountInspectorService;

    @Autowired
    private TrackingEntityRepository trackingEntityRepository;

    @Autowired
    private ClientGHSugarRepository clientGHSugarRepository;

    @Autowired
    private ReportGeneratorService reportGeneratorService;


    @GetMapping(value = "ma-accounts-report")
    public String getAdvisorsAccountOverview(Model model,
                                             @RequestParam(value = "limit",
                                                     required = false,
                                                     defaultValue = "0") Integer limit,
                                             @RequestParam(value = "replace",
                                                     required = true) Boolean replace) {

        List<TrackingEntity> trackingEntities = new ArrayList<>();

        try {
            trackingEntities.addAll(gaAccountInspectorService.getTrackinEntityReport(limit));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (TrackingEntity trackingEntity : trackingEntities) {
            try {
                SiteExtractionReport siteExtractionReport = siteMetadataExtractor.getExtractionReport(trackingEntity.getWebsite());
                List<String> codes = siteExtractionReport.getUaCodes();
                trackingEntity.addWebsiteActualCodes(codes);

                StringBuilder sb = new StringBuilder();
                codes.forEach(code -> {
                    sb.append(code + ",");
                });
                trackingEntity.setWebsiteActualCodes(sb.toString());

                trackingEntity.setSendingGAcodes(siteExtractionReport.getSendOccurences());

            } catch (Exception e) {
                trackingEntity.setWebsiteActualCodes("");

            }
        }

        model.addAttribute("trackingEntities", trackingEntities);

        if (replace) {
            trackingEntityRepository.deleteAll();
            trackingEntityRepository.saveAll(trackingEntities);
        }
        reportGeneratorService.setTrackingEntities(trackingEntities);

        return "ma-accounts-report";
    }


    @GetMapping(value = "get-configuration-report")
    public String getConfigurationReport(Model model) {
        List<ClientGHSugar> ghClients = initLoadClientTable();

        Iterable<TrackingEntity> trackingEntities = trackingEntityRepository.findAll();
        reportGeneratorService.setTrackingEntities(Lists.newArrayList(trackingEntities));

        Map<String, List<TrackingEntity>> supportingTrackingEntitiesForHostNames =
                reportGeneratorService.getSupportingTrackingEntitiesForHostNames();


        Map<String, List<TrackingEntity>> supportingTrackingEntitiesForHostNames_filtered = new HashMap<>();

        //get the really supported clients not just by account only - we assume 2 tracking accounts max
        supportingTrackingEntitiesForHostNames.forEach((key, value) -> {
            if (value.size() == 2)
                supportingTrackingEntitiesForHostNames_filtered.put(key, value);
        });

        Map<String, Pair<List<TrackingEntity>, ClientGHSugar>> trackingAccountsWithGhClientDashboard = new HashMap<>();

        /*List<ClientGHSugar> foundSugarClients = new ArrayList<>();*/

        Set<Map.Entry<String, List<TrackingEntity>>> entries = supportingTrackingEntitiesForHostNames_filtered.entrySet();
        for (Map.Entry<String, List<TrackingEntity>> entry : entries) {
            String key = entry.getKey();
            List<TrackingEntity> trackingEntitiesList = entry.getValue();
            ClientGHSugar foundClient = null;
            for (TrackingEntity trackingEntity : trackingEntitiesList) {
                //trying to match sugar client to tracking account by name ignoring case
                foundClient = clientGHSugarRepository.findByNameIgnoreCaseContaining(trackingEntity.getAccountName());
                if (foundClient != null) {
                    break;
                }

                foundClient = clientGHSugarRepository.findByWebsiteIgnoreCaseContaining(
                        HttpReformatter.getCoreHostName(trackingEntity.getWebsite()));
                if (foundClient != null) {
                    break;
                }
            }
            trackingAccountsWithGhClientDashboard.put(key, Pair.with(trackingEntitiesList, foundClient));
        }


        //model.addAttribute("foundClients", foundSugarClients);

        model.addAttribute("idiumSugarClientsCount", ghClients.size());
        //model.addAttribute("matchedConfigs", supportingTrackingEntitiesForHostNames);
        model.addAttribute("matchedConfigs", supportingTrackingEntitiesForHostNames_filtered);

        model.addAttribute("matchedConfigsWithGh", trackingAccountsWithGhClientDashboard);


        model.addAttribute("ghClientDashboard", "http://growth-house.idium.no/dashboard/");

        return "configuration-report";
    }


    @GetMapping(value = "get-clients-report")
    public String getSugarClientsReport(Model model) {

        List<ClientGHSugar> ghClients = initLoadClientTable();

        model.addAttribute("idiumSugarClientsCount", ghClients.size());
        model.addAttribute("ghClients", ghClients);
        model.addAttribute("ghCustomerOverview", "http://growth-house.idium.no/customer-overview/");
        model.addAttribute("ghClientDashboard", "http://growth-house.idium.no/dashboard/");
        model.addAttribute("sugarLink", "https://crm.idium.no/index.php?module=Accounts&action=DetailView&record=");

        return "sugar-clients-report";
    }


    @GetMapping(value = "get-cached-report")
    public String getCachedReport(Model model) {
        model.addAttribute("trackingEntities", trackingEntityRepository.findAll());
        return "ma-accounts-report";
    }


    @RequestMapping(value = "/check-host-gh-tracked", method = RequestMethod.GET)
    public String checkHostTrackedByGrowthHouse(@RequestParam(value = "address") String hostName,
                                                Model model) throws Exception {
        List<TrackingEntity> trackingAccountsDetails = trackingGHAccountsService.getInitializeTrackingAccountsDetails();
        for (TrackingEntity te : trackingAccountsDetails) {
            logger.info(te.toString());
        }

        List<String> uaCodes = siteMetadataExtractor.getExtractionReport(hostName).getUaCodes();

        List<TrackingEntity> trackedHostGrowthHouseReport = new ArrayList<>();
        if (uaCodes.size() > 0) {
            trackedHostGrowthHouseReport.addAll(trackingGHAccountsService.getTrackedHostGrowthHouseReport(uaCodes));
        } else
            logger.info(" NO TRACKING CODES extracted for: " + hostName);

        model.addAttribute("trackingAccounts", trackedHostGrowthHouseReport);
        model.addAttribute("trackedMessage", trackedHostGrowthHouseReport.size() > 0);
        model.addAttribute("trackedHost", hostName);

        return "tracked-report";
    }


    @RequestMapping(value = "/check-host-codes", method = RequestMethod.GET)
    public String checkHostName(@RequestParam(value = "address") String addressToCheck,
                                Model model) throws Exception {

        List<String> uaCodes = new ArrayList<>();

        SiteExtractionReport siteExtractionReport;
        Integer sendOccurrences = 0;

        try {
            siteExtractionReport = siteMetadataExtractor.getExtractionReport(addressToCheck);
            uaCodes.addAll(siteExtractionReport.getUaCodes());
            sendOccurrences = siteExtractionReport.getSendOccurences();

        } catch (Exception e) {
            logger.info("problem accessing host: " + addressToCheck);
        }

        model.addAttribute("hostName", addressToCheck);
        model.addAttribute("codesExtracted", uaCodes);
        model.addAttribute("sendOccurrences", sendOccurrences);

        return "host-check";
    }

    private List<ClientGHSugar> initLoadClientTable() {
        List<ClientGHSugar> ghClients;

        if (clientGHSugarRepository.count() == 0) {
            logger.info("no sugar clients in database yet. Reading and caching...");
            ghClients = dynamoDBclientDataExtractor.getClientSugarData();
            clientGHSugarRepository.saveAll(ghClients);
            logger.info("GrowthHouse clients cached in database : " + ghClients.size() + " entries.");
        } else {
            logger.info("restoring client data from database...");
            Iterable<ClientGHSugar> ghSugarRepositoryAll = clientGHSugarRepository.findAll();
            ghClients = new ArrayList<>();
            ghSugarRepositoryAll.iterator().forEachRemaining(client -> ghClients.add(client));
            logger.info("restored from local database: " + ghClients.size() + " entries");
        }
        return ghClients;
    }

}
