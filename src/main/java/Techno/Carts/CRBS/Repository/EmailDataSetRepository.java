package Techno.Carts.CRBS.Repository;

import Techno.Carts.CRBS.Entity.EmailDataSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailDataSetRepository extends JpaRepository<EmailDataSet, Long> {

    EmailDataSet findByEmail(String email);
}

