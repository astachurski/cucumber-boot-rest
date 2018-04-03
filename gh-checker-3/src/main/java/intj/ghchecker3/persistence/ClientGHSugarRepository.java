package intj.ghchecker3.persistence;

import intj.ghchecker3.domain.ClientGHSugar;
import org.springframework.data.repository.CrudRepository;

public interface ClientGHSugarRepository extends CrudRepository<ClientGHSugar, Long> {
    ClientGHSugar findByName(String name);

    ClientGHSugar findByNameIgnoreCaseContaining(String name);

    ClientGHSugar findByWebsiteIgnoreCaseContaining(String hostName);
}
