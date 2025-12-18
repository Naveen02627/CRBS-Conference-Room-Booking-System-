package Techno.Carts.CRBS.Repository;

import Techno.Carts.CRBS.Entity.Hall;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends MongoRepository<Hall, String> {

    List<Hall> findByLocationStateAndLocationCity(String state, String city);
}

