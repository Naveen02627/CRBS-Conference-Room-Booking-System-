package com.TechnoCarts.CRBS.Repository;

import com.TechnoCarts.CRBS.Dto.RequestBookingDto;
import com.TechnoCarts.CRBS.Entity.BookingRequest;
import com.TechnoCarts.CRBS.Entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

    @Query("SELECT r FROM BookingRequest r WHERE r.status = :status")
    List<BookingRequest> findRequestWhereStatusIsRequested(Status status);
}

