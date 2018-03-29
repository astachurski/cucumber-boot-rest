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
    RestTemplate restTemplate;

    public List<String> getUACodes(String hostName) throws Exception {
        List<String> localResult = new ArrayList<>();
        String resp = restTemplate.getForObject(hostName, String.class);

        if (resp != null) {
            Pattern pattern = Pattern.compile("UA-\\d{8}\\d{0,1}-\\d");
            Matcher matcher = pattern.matcher(resp);
            while (matcher.find()) {
                localResult.add(matcher.group().trim());
            }
        } else {
            System.out.println(" --------------- failed retrieving data for : " + hostName);
        }
        return localResult;
    }

}
