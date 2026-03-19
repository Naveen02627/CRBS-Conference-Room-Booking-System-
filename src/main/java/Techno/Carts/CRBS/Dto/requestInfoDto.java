package Techno.Carts.CRBS.Dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class requestInfoDto {
    private List<Integer> slots;
    private String purpose;
    private String Address;
}
