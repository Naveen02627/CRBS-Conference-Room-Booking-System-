package com.TechnoCarts.CRBS.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestBookingDto {
    private String purpose;
    private List<Integer> slots;
    private LocalDate RequestedDate;
    private Long HallId;
}
