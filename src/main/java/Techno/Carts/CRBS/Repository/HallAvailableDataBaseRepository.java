package Techno.Carts.CRBS.Repository;


import Techno.Carts.CRBS.Entity.HallAvailableDataBase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HallAvailableDataBaseRepository extends MongoRepository<HallAvailableDataBase, String> {
    Optional<HallAvailableDataBase> findByState(String state);

}
