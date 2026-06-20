package com.TechnoCarts.CRBS.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UpdateHall {
    private boolean isActive;
    private String name;
    private String Details;
    private String imageUrl;
}
