package com.TechnoCarts.CRBS.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class BookingRequest {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long hallId;

    private LocalDate requestedDate;

    @ElementCollection
    @CollectionTable(
            name = "booking_slots",
            joinColumns = @JoinColumn(name = "booking_id")
    )
    @Column(name = "slot")
    private List<Integer> slots;

    private String purpose;

    private Status status;

    private LocalDateTime requestedAt;

}
