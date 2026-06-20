package com.TechnoCarts.CRBS.Repository;


import com.TechnoCarts.CRBS.Entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HallRepository extends JpaRepository<Hall, Long> {

    @Query("SELECT h FROM Hall h WHERE h.name =:name")
    Optional<Hall> findByHallName(@Param("name") String hallName);



}
