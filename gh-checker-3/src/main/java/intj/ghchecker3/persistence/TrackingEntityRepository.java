package intj.ghchecker3.persistence;

import intj.ghchecker3.TrackingEntity;
import org.springframework.data.repository.CrudRepository;

public interface TrackingEntityRepository extends CrudRepository<TrackingEntity, Long> {
}
