package com.TechnoCarts.CRBS.Controller;

import com.TechnoCarts.CRBS.Dto.HallResponse;
import com.TechnoCarts.CRBS.Dto.RequestBookingDto;
import com.TechnoCarts.CRBS.Entity.BookingRequest;
import com.TechnoCarts.CRBS.Services.BookingService.CheckAvailability;
import com.TechnoCarts.CRBS.Services.BookingService.RequestForBooking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class BookingController {

    private final CheckAvailability checkAvailability;
    private final RequestForBooking requestForBooking;

    @GetMapping("/getHall")
    public ResponseEntity<?> getAvailableHall(@RequestParam LocalDate date) {
        log.info("in Controller getAvailableHall");
        List<HallResponse> resp = checkAvailability.getHall(date).getBody();

        return ResponseEntity.ok(resp);
    }
    @PostMapping("/request")
    public ResponseEntity<?> CreateRequest(@RequestBody RequestBookingDto dto) {
        log.info("in Controller CreateRequest");
        return ResponseEntity.ok(requestForBooking.CreateRequest(dto));
    }

}
