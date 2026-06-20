package com.TechnoCarts.CRBS.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class hallDTO {

    private String hallName;
    private String location;
    private String Details;
    private String imageUrl;

}
