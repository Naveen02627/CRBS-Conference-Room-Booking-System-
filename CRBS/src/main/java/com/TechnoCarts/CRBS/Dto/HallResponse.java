package com.TechnoCarts.CRBS.Dto;


import com.TechnoCarts.CRBS.Entity.pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HallResponse {
    private pair[] slotsBooked;
    private String hallName;
    private String Details;
    private String ImageUrl;

}
