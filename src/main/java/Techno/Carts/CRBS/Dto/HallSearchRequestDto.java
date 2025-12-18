package Techno.Carts.CRBS.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class HallSearchRequestDto {
    private String state;
    private String city;
    private LocalDate requestedDate;

}
