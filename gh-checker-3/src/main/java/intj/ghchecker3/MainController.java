package intj.ghchecker3;

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

        List<String> hostsToCheck = new ArrayList<>();
        if (addressToCheck != null)
            hostsToCheck.add(addressToCheck);
        else
            hostsToCheck.addAll(hostNameService.hostNames());

        int checkedOk = 0;
        int checkedBad = 0;
        int uaCodesExtracted = 0;

        List<String> uaCodes = new ArrayList<>();

        for (String hostName : hostsToCheck) {
            logger.info("-- extracting metadata for: " + hostName);
            try {
                uaCodes.addAll(siteMetadataExtractor.getExtractionReport(hostName).getUaCodes());
                checkedOk++;
                for (String uaCode : uaCodes) {
                    logger.info("extracted UA code:   " + uaCode);
                    uaCodesExtracted++;
                }
            } catch (Exception e) {
                checkedBad++;

                logger.info(" ===== problem with site: " + hostName + "  - " + e.getMessage());
            }
        }

        logger.info(" hosts checked successfully : " + checkedOk);
        logger.info("   tracking codes extracted : " + uaCodesExtracted);
        logger.info(" hosts NOT checked successfully : " + checkedBad);

        model.addAttribute("hostName", addressToCheck);
        model.addAttribute("codesExtracted", uaCodes);

        return "host-check";
    }
}
