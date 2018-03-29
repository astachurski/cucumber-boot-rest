package intj.ghchecker3;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(scopeName = "singleton")
public class TrackingAccountsService {

    private final Map<String, TrackingEntity> trackingEntitiesMap = new HashMap<>();

    public List<TrackingEntity> getTrackingAccountsDetails() throws Exception {

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
                        split[3].trim());

                this.trackingEntitiesMap.put(trackingEntity.getTrackingId(), trackingEntity);
                trackingEntities.add(trackingEntity);

            }
        }
        return trackingEntities;
    }

}
