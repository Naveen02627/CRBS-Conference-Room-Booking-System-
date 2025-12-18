package Techno.Carts.CRBS.Repository;

import Techno.Carts.CRBS.Entity.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Long> {
}