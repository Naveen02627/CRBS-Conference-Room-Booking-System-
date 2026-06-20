package com.TechnoCarts.CRBS.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private boolean isActive;
    private String details;
    private String ImageUrl;
    @Lob
    private ConcurrentHashMap<LocalDate, pair[]> slots = new ConcurrentHashMap<>();

}
