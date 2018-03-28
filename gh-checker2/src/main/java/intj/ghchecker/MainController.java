package intj.ghchecker;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MainController {


    @Autowired
    private TrackingAccountsService trackingAccountsService;

    @Autowired
    private HostNameService hostNameService;

    @Autowired
    private SiteMetadataExtractor siteMetadataExtractor;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(Model model) throws Exception {

        List<TrackingEntity> trackingAccountsDetails = trackingAccountsService.getTrackingAccountsDetails();

        model.addAttribute("trackingAccountsDetails", trackingAccountsDetails);

        /*for (TrackingEntity te: trackingAccountsDetails){
            System.out.println(te.toString());

        }*/

        //List<String> hostsToCheck = hostNameService.hostNames();

        return "main";

    }



    @GetMapping(value = "/test")
    @ResponseBody
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
