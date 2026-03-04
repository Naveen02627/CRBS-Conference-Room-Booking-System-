package Techno.Carts.CRBS.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Builder
public class HallSearchResponseDto {
    private String hallName;
    private String hallId;
    private String address;
    private String state;
    private String city;
    private String imageUrl;
    private double price;
    private String description;
    private int capacity;
    private List<Integer> bookedSlots;


}
