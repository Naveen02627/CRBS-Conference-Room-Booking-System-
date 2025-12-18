package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Dto.HallSearchRequestDto;
import Techno.Carts.CRBS.Dto.HallSearchResponseDto;
import Techno.Carts.CRBS.Entity.Hall;
import Techno.Carts.CRBS.Services.BookingSearchServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/search")
@AllArgsConstructor
public class BookingSearchController {

    private final BookingSearchServices bookingSearchServices;

    @GetMapping("/rooms")
    public ResponseEntity<List<HallSearchResponseDto>> getRooms(
            @RequestParam String state,@RequestParam String city,@RequestParam LocalDate date) {
        HallSearchRequestDto requestDto = new HallSearchRequestDto();
        requestDto.setRequestedDate(date);
        requestDto.setCity(city);
        requestDto.setState(state);

        return ResponseEntity.ok(
                bookingSearchServices.getAvailableHalls(requestDto)
        );
    }
}

