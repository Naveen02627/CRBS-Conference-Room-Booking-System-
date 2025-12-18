package Techno.Carts.CRBS.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private Long UserId;

    private String purpose;

    private LocalDate requestedDate;

    private int slot;

    private LocalDate CreatedAt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime LastUpdate;
}
