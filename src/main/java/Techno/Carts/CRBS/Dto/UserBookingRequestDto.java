package Techno.Carts.CRBS.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;



@Builder
@Data
public class UserBookingRequestDto {
    @NotBlank
    private String hallId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestedDate;

    @NotEmpty(message = "At least one slot must be selected")
    private List<Integer> slot;

    @NotBlank
    private String purpose;
}