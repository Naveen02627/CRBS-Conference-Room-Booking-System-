package Techno.Carts.CRBS.Repository;

import Techno.Carts.CRBS.Entity.BookingRequest;
import Techno.Carts.CRBS.Entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {
    List<BookingRequest> findByStatus(Status status);
}