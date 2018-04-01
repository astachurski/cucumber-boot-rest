package intj.ghchecker3.services;

import intj.ghchecker3.domain.SiteExtractionReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SiteMetadataExtractor {

    private static Logger logger = LoggerFactory.getLogger("SiteMetadataExtractor");

    @Autowired
    private RestTemplate restTemplate;

    public SiteExtractionReport getExtractionReport(String hostName) throws Exception {

        SiteExtractionReport siteExtractionReport = new SiteExtractionReport();
        List<String> localResultUAcodes = new ArrayList<>();
        List<String> localResultDoubleSend1 = new ArrayList<>();
        String resp = restTemplate.getForObject(hostName, String.class);

        if (resp != null) {
            Pattern patternUAcodes = Pattern.compile("UA-\\d{6,9}-\\d");
            Matcher matcherUAcodes = patternUAcodes.matcher(resp);
            while (matcherUAcodes.find()) {
                localResultUAcodes.add(matcherUAcodes.group().trim());
            }

            siteExtractionReport.setUaCodes(localResultUAcodes);

            String patternStr = "ga\\('[a-z]*\\.*send'|__gaTracker\\('send'|_trackPageview'\\]";
            Pattern patternDoubleSend1 = Pattern.compile(patternStr);


            Matcher matcherDoubleSend1 = patternDoubleSend1.matcher(resp);
            //boolean skipGroupZero = true;
            while (matcherDoubleSend1.find()) {
                // if (!skipGroupZero)
                localResultDoubleSend1.add(matcherDoubleSend1.group().trim());
                // else
                // skipGroupZero = false;

            }

            //https://www.freeformatter.com/java-regex-tester.html#ad-output

            siteExtractionReport.setSendOccurences(localResultDoubleSend1.size());


        } else {
            logger.info(" --------------- failed retrieving data for : " + hostName);
        }
        return siteExtractionReport;
    }

}
