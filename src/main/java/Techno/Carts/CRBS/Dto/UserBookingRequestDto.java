package Techno.Carts.CRBS.Dto;

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


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBookingRequestDto {

    @NotBlank(message = "Hall ID is required")
    private String hallId;

    @NotNull(message = "Requested date and time is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestedDate;

    private List<Integer> slot;

    @NotBlank(message = "Purpose is required")
    private String purpose;
}