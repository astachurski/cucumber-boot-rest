package intj.ghchecker3.services;

import intj.ghchecker3.domain.TrackingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportGeneratorService {

    private static Logger logger = LoggerFactory.getLogger("ReportGeneratorService");

    //UA-code, AccountProperty (tracking entity)
    private Map<String, TrackingEntity> trackingEntityMap = new HashMap<>();

    public void setTrackingEntities(List<TrackingEntity> trakingEntities) {
        trakingEntities.forEach(entity -> trackingEntityMap.put(entity.getTrackingId(), entity));
        logger.info("currently registered: " + trackingEntityMap.size() + " tracking entities");
    }

    //returns hostname with set of tracking accounts (properties) pointed by it
    public Map<String, List<TrackingEntity>> getSupportingTrackingEntitiesForHostNames() {

        Map<String, List<TrackingEntity>> result = new HashMap<>();

        for (TrackingEntity trackingEntity : trackingEntityMap.values()) {

            List<String> codesToTest = trackingEntity.getWebsiteActualCodesList();

            List<TrackingEntity> trackingEntitiesForCodePartialList = codesToTest.stream()
                    .map(code -> trackingEntityMap.get(code)).filter(entity -> entity != null).collect(Collectors.toList());

            if (trackingEntitiesForCodePartialList.size() > 0) {
                result.put(trackingEntity.getWebsite(), trackingEntitiesForCodePartialList);

            }

        }
        return result;
    }
}
