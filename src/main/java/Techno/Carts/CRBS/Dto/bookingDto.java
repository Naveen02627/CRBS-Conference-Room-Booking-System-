package Techno.Carts.CRBS.Dto;

import Techno.Carts.CRBS.Entity.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class bookingDto {

    private String username;
    private String hallName;
    private LocalDate date;
    private long requestId;


}
