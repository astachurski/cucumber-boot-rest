package intj.ghchecker3;

import intj.ghchecker3.persistence.TrackingEntityRepository;
import intj.ghchecker3.services.GaAccountInspectorService;
import intj.ghchecker3.services.HostNameService;
import intj.ghchecker3.services.TrackingGHAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger("MainController");

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


    @GetMapping(value = "ma-accounts-report")
    public String getAdvisorsAccountOverview(Model model,
                                             @RequestParam(value = "limit",
                                                     required = false,
                                                     defaultValue = "0") Integer limit) {

        List<TrackingEntity> accountsReports = new ArrayList<>();

        try {
            accountsReports.addAll(gaAccountInspectorService.getTrackinEntityReport(limit));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (TrackingEntity trackingEntity : accountsReports) {
            try {
                SiteExtractionReport siteExtractionReport = siteMetadataExtractor.getExtractionReport(trackingEntity.getWebsite());
                List<String> codes = siteExtractionReport.getUaCodes();
                StringBuilder sb = new StringBuilder();
                codes.forEach(code -> sb.append(code + ","));
                trackingEntity.setWebsiteActualCodes(sb.toString());
                trackingEntity.setSendingGAcodes(siteExtractionReport.getSendOccurences());

            } catch (Exception e) {
                trackingEntity.setWebsiteActualCodes("--?--");

            }
        }

        model.addAttribute("trackingEntities", accountsReports);

        trackingEntityRepository.saveAll(accountsReports);

        return "ma-accounts-report";
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
}
