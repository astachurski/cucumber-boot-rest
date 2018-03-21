package intj.ghchecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class MainController {


    @Autowired
    private TrackingAccountsService trackingAccountsService;

    @Autowired
    private HostNameService hostNameService;

    @Autowired
    private SiteMetadataExtractor siteMetadataExtractor;

    @GetMapping(value = "/test")
    public String test(@RequestParam(value = "address") String addressToCheck) throws Exception {

        List<TrackingEntity> trackingAccountsDetails = trackingAccountsService.getTrackingAccountsDetails();

        /*for (TrackingEntity te: trackingAccountsDetails){
            System.out.println(te.toString());

        }*/

        List<String> hostsToCheck = hostNameService.hostNames();

        int checkedOk = 0;
        int checkedBad = 0;
        int uaCodesExtracted = 0;

        for (String hostName : hostsToCheck) {

            System.out.println("-- extracting metadata for: " + hostName);

            try {
                List<String> uaCodes = siteMetadataExtractor.getUACodes(hostName);
                checkedOk ++;
                for (String uaCode : uaCodes) {
                    System.out.println(uaCode);
                    uaCodesExtracted ++;
                }
            } catch (Exception e) {
                checkedBad ++;

                System.out.println(" ===== problem with site: " + hostName + "  - " + e.getMessage());

            }

        }

        System.out.println(" -------------- ");
        System.out.println(" hosts checked successfully : " + checkedOk);
        System.out.println("   tracking codes extracted : "  + uaCodesExtracted);
        System.out.println(" hosts NOT checked successfully : " + checkedOk);

        return "bub";
    }
}
