package Techno.Carts.CRBS.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long actionById;
    private String action;   // APPROVED / REJECTED
    private String remark;
    private Long userId;     // renamed to camelCase (convention)

    private String purpose;
    private LocalDate requestedDate;


    @ElementCollection
    @CollectionTable(
            name = "booking_history_slots",
            joinColumns = @JoinColumn(name = "history_id")
    )
    @Column(name = "slot_id")
    private List<Integer> slots = new ArrayList<>();


    private LocalDate createdAt;

    @CreatedDate
    private LocalDate lastUpdate;
}