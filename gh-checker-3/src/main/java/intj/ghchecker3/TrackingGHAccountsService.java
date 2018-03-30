package intj.ghchecker3;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Scope(scopeName = "singleton")
public class TrackingGHAccountsService {

    private final Map<String, TrackingEntity> trackingEntitiesMap = new HashMap<>();

    @PostConstruct
    public void init() throws Exception {
        File file = new File("/home/adrian/Development/SDA/Spring/gh-checker2/src/main/resources/static/GH-accounts");

        List<TrackingEntity> trackingEntities = new ArrayList<>();

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            line = bufferedReader.readLine();
            if (line != null) {
                String[] split = line.split("\\|");

                TrackingEntity trackingEntity = new TrackingEntity(0L,
                        split[1].trim(),
                        split[2].trim(),
                        split[3].trim(),
                        "---");

                trackingEntity.setGrowthHouse(true);
                this.trackingEntitiesMap.put(trackingEntity.getTrackingId(), trackingEntity);
                //trackingEntities.add(trackingEntity);

            }
        }
    }

    public List<TrackingEntity> getInitializeTrackingAccountsDetails() throws Exception {
        return trackingEntitiesMap.values().stream().collect(Collectors.toList());
    }

    public List<TrackingEntity> getTrackedHostGrowthHouseReport(List<String> hostTrackingUAcodes) {

        List<TrackingEntity> results = new ArrayList<>();

        for (String uaHostCode : hostTrackingUAcodes) {
            if (this.trackingEntitiesMap.get(uaHostCode) != null) {
                results.add(this.trackingEntitiesMap.get(uaHostCode));
            }
        }

        return results;
    }

    public Boolean isTrackingEntityGrowthHouse(TrackingEntity foreginTrackingEntity) {
        return this.trackingEntitiesMap.get(foreginTrackingEntity.getTrackingId()) != null;
    }

}
