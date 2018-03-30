package intj.ghchecker3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SiteMetadataExtractor {

    @Autowired
    private RestTemplate restTemplate;

    public SiteExtractionReport getExtractionReport(String hostName) throws Exception {

        SiteExtractionReport siteExtractionReport = new SiteExtractionReport();
        List<String> localResultUAcodes = new ArrayList<>();
        List<String> localResultDoubleSend1 = new ArrayList<>();
        String resp = restTemplate.getForObject(hostName, String.class);

        if (resp != null) {
            Pattern patternUAcodes = Pattern.compile("UA-\\d{8}\\d{0,1}-\\d");
            Matcher matcherUAcodes = patternUAcodes.matcher(resp);
            while (matcherUAcodes.find()) {
                localResultUAcodes.add(matcherUAcodes.group().trim());
            }

            siteExtractionReport.setUaCodes(localResultUAcodes);

            Pattern patternDoubleSend1 = Pattern.compile("ga\\('send'");
            Matcher matcherDoubleSend1 = patternDoubleSend1.matcher(resp);
            while (matcherDoubleSend1.find()) {
                localResultDoubleSend1.add(matcherDoubleSend1.group().trim());
            }

            siteExtractionReport.setSendOccurences(localResultDoubleSend1.size());


        } else {
            System.out.println(" --------------- failed retrieving data for : " + hostName);
        }
        return siteExtractionReport;
    }

}
