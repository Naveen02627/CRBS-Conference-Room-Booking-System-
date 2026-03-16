package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Dto.HodDecisionDto;
import Techno.Carts.CRBS.Dto.UserBookingRequestDto;
import Techno.Carts.CRBS.Entity.BookingRequest;
import Techno.Carts.CRBS.Entity.Status;
import Techno.Carts.CRBS.Repository.BookingRequestRepository;
import Techno.Carts.CRBS.Services.BookingSearchServices;
import Techno.Carts.CRBS.Services.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
//@CrossOrigin(origins = "*")
@RequestMapping("/booking")
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;
    private final BookingRequestRepository bookingRequestRepository;

    @PostMapping("/request")
    public ResponseEntity<?> createRequest(@RequestBody @Valid UserBookingRequestDto dto) {
        return bookingService.requestToHOD(dto);
    }
    @GetMapping("/hod/pending")   // ← add this if it's in controller
    public ResponseEntity<List<BookingRequest>> getHodPendingRequests() {

        log.info("HOD fetching pending requests (status = PENDING_HOD)");

        List<BookingRequest> requests = bookingRequestRepository.findByStatusOrderByCreatedAtDesc(Status.PENDING_HOD);
        if (requests.isEmpty()) {
            log.info("No pending requests found for HOD");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());  // better: return empty list instead of empty body
        }

        log.info("Found {} pending requests for HOD", requests.size());

        return ResponseEntity.ok(requests);
    }
    @PostMapping("/hod/accept/{requestId}")
    public ResponseEntity<?> acceptByHod(@PathVariable Long requestId) {
        log.info("HOD accept request with id: {}", requestId);
        return bookingService.acceptByHod(requestId);
    }

    @PostMapping("/hod/reject")
    public ResponseEntity<?> requestRejectedHOD(
            @RequestBody HodDecisionDto dto) {

        return bookingService.rejectedByHOD(
                dto.getRequestId(),
                dto.getRemark()
        );
    }

//
    @PostMapping("/ADMINAccept/{id}")
    public ResponseEntity<?> requestAcceptedByAdmin(@PathVariable Long id){
        return bookingService.acceptedByAdmin(id);
    }

    @PostMapping("/ADMINReject")
    public ResponseEntity<?> requestRejectedByAdmin(@RequestBody HodDecisionDto dto ){
        return bookingService.rejectedByAdmin(dto.getRequestId(), dto.getRemark());
    }

    @GetMapping("/pendingrequestADMIN")
    public ResponseEntity<List<BookingRequest>> getListOfAdminPending(){
        log.info("ADMIN fetching pending requests (status = PENDING_ADMIN)");
        return bookingService.AdminPendingRequest();
    }
//

}
