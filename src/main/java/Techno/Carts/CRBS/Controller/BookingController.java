package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Dto.HodDecisionDto;
import Techno.Carts.CRBS.Dto.UserBookingRequestDto;
import Techno.Carts.CRBS.Entity.BookingRequest;
import Techno.Carts.CRBS.Services.BookingSearchServices;
import Techno.Carts.CRBS.Services.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/request")
    public ResponseEntity<?> RequestToHOD(@RequestBody UserBookingRequestDto userBookingRequestDto){

        return bookingService.requestToHOD(userBookingRequestDto);
    }
    @GetMapping("/pendingRequestHOD")
    public ResponseEntity<List<BookingRequest>> getAllPendingRequest(){

        return bookingService.HodPendingRequest();
    }
    @PostMapping("/HODAccept")
    public ResponseEntity<?> requestAcceptHOD(@RequestBody Long id) {
        return bookingService.acceptByHod(id);
    }

    @PostMapping("/HODReject")
    public ResponseEntity<?> requestRejectedHOD(
            @RequestBody HodDecisionDto dto) {

        return bookingService.rejectedByHOD(
                dto.getRequestId(),
                dto.getRemark()
        );
    }


    @PostMapping("/ADMINAccept")
    public ResponseEntity<?> requestAcceptedByAdmin(@RequestBody Long id){
        return bookingService.acceptedByAdmin(id);
    }

    @PostMapping("/ADMINReject")
    public ResponseEntity<?> requestRejectedByAdmin(@RequestBody HodDecisionDto dto ){
        return bookingService.rejectedByAdmin(dto.getRequestId(), dto.getRemark());
    }

    @GetMapping("/pendingRequestADMIN")
    public ResponseEntity<List<BookingRequest>> getListOfAdminPending(){
        return bookingService.AdminPendingRequest();
    }


}
