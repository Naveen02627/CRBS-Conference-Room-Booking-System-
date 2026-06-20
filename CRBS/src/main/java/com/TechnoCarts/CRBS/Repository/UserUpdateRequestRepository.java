package com.TechnoCarts.CRBS.Repository;

import com.TechnoCarts.CRBS.Entity.Status;
import com.TechnoCarts.CRBS.Entity.UserUpdateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserUpdateRequestRepository extends JpaRepository<UserUpdateRequest, Long> {

    @Query("SELECT r FROM UserUpdateRequest r WHERE r.status = :status")
    List<UserUpdateRequest> findAllByStatus(@Param("status") Status status);


}
