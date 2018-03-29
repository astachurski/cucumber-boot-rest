package intj.ghchecker3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger("MainController");


    @Autowired
    private TrackingAccountsService trackingAccountsService;

    @Autowired
    private HostNameService hostNameService;

    @Autowired
    private SiteMetadataExtractor siteMetadataExtractor;

    @Autowired
    private GaAccountInspectorService gaAccountInspectorService;


    @GetMapping(value = "report")
    @ResponseBody
    public String getAdvisorsAccountReports() {

        List<AccountReportItem> accountsReports = null;
        try {
            accountsReports = gaAccountInspectorService.getAccountsReports();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accountsReports.toString();

    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(Model model) throws Exception {

        List<TrackingEntity> trackingAccountsDetails = trackingAccountsService.getTrackingAccountsDetails();

        model.addAttribute("trackingAccountsDetails", trackingAccountsDetails);

        for (TrackingEntity te : trackingAccountsDetails) {
            System.out.println(te.toString());

        }

        //List<String> hostsToCheck = hostNameService.hostNames();

        return "main";

    }

    @RequestMapping(value = "/check-tracking", method = RequestMethod.GET)
    @ResponseBody
    public String checkTrackingAccounts() throws Exception {

        List<TrackingEntity> trackingAccountsDetails = trackingAccountsService.getTrackingAccountsDetails();

        for (TrackingEntity te : trackingAccountsDetails) {
            logger.info(te.toString());
        }

        return "";
    }

    @RequestMapping(value = "/check-host-gh-tracked", method = RequestMethod.GET)
    public String checkHostTrackedByGrowthHouse(@RequestParam(value = "address") String hostName,
                                                Model model) throws Exception {
        List<TrackingEntity> trackingAccountsDetails = trackingAccountsService.getTrackingAccountsDetails();
        for (TrackingEntity te : trackingAccountsDetails) {
            logger.info(te.toString());
        }

        List<String> uaCodes = siteMetadataExtractor.getUACodes(hostName);

        List<TrackingEntity> trackedHostGrowthHouseReport = new ArrayList<>();
        if (uaCodes.size() > 0) {
            trackedHostGrowthHouseReport.addAll(trackingAccountsService.getTrackedHostGrowthHouseReport(uaCodes));
        } else
            logger.info(" NO TRACKING CODES extracted for: " + hostName);

        model.addAttribute("trackingAccounts", trackedHostGrowthHouseReport);

        model.addAttribute("trackedMessage", trackedHostGrowthHouseReport.size() > 0);

        model.addAttribute("trackedHost", hostName);

        return "tracked-report";
    }





    @RequestMapping(value = "/check-host", method = RequestMethod.GET)
    @ResponseBody
    public String checkHostName(@RequestParam(value = "address") String addressToCheck) throws Exception {

        List<String> hostsToCheck = new ArrayList<>();
        if (addressToCheck != null)
            hostsToCheck.add(addressToCheck);
        else
            hostsToCheck.addAll(hostNameService.hostNames());

        int checkedOk = 0;
        int checkedBad = 0;
        int uaCodesExtracted = 0;

        for (String hostName : hostsToCheck) {

            System.out.println("-- extracting metadata for: " + hostName);

            try {
                List<String> uaCodes = siteMetadataExtractor.getUACodes(hostName);
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

        return "bub";
    }
}
