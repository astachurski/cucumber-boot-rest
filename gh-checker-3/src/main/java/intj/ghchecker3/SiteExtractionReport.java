package intj.ghchecker3;

import java.util.ArrayList;
import java.util.List;

public class SiteExtractionReport {

    private List<String> uaCodes;
    private Integer sendOccurences;

    public List<String> getUaCodes() {
        return uaCodes;
    }

    public void setUaCodes(List<String> uaCodes) {
        this.uaCodes = uaCodes;
    }

    public Integer getSendOccurences() {
        return sendOccurences;
    }

    public void setSendOccurences(Integer sendOccurences) {
        this.sendOccurences = sendOccurences;
    }
}
