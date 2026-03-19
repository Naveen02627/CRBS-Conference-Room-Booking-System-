package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Dto.HodDecisionDto;
import Techno.Carts.CRBS.Dto.UserBookingRequestDto;
import Techno.Carts.CRBS.Dto.bookingDto;
import Techno.Carts.CRBS.Dto.requestInfoDto;
import Techno.Carts.CRBS.Entity.*;
import Techno.Carts.CRBS.Repository.BookingRequestRepository;
import Techno.Carts.CRBS.Repository.DepartmentRepository;
import Techno.Carts.CRBS.Repository.HallRepository;
import Techno.Carts.CRBS.Repository.UserRepository;
import Techno.Carts.CRBS.Services.BookingSearchServices;
import Techno.Carts.CRBS.Services.BookingService;
import Techno.Carts.CRBS.Services.CurrentUser;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/booking")

public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;
    private final BookingRequestRepository bookingRequestRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final CurrentUser currentUser;
    private final HallRepository hallRepository;

    @PostMapping("/request")
    public ResponseEntity<?> createRequest(@RequestBody @Valid UserBookingRequestDto dto) {
        return bookingService.requestToHOD(dto);
    }
    @GetMapping("/hod/pending")
    public ResponseEntity<List<bookingDto>> getHodPendingRequests() {

        log.info("HOD fetching pending requests (status = PENDING_HOD)");

        // 1. Get all pending requests
        List<BookingRequest> requests = bookingRequestRepository
                .findByStatusOrderByCreatedAtDesc(Status.PENDING_HOD);

        List<bookingDto> dtoList = new ArrayList<>();
        log.info("HOD fetching pending requests (status = PENDING_HOD)");

        // Check if this request belongs to HOD's department
        long currentUserDeptId = currentUser.getDepartmentId();
        log.info("HOD DeptId  {}", currentUserDeptId );

        for (BookingRequest req : requests) {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> {
                        log.error("User not found for id: {}", req.getUserId());
                        return new RuntimeException("User not found for id: " + req.getUserId());
                    });
            log.info("User fetching pending request (status = PENDING_HOD): {}", user);
            // Fetch Department
            Department dept = departmentRepository.findById(user.getDepartmentId())
                    .orElseThrow(() -> {
                        log.error("Department not found for id: {}", user.getDepartmentId());
                        return new RuntimeException("Department not found");
                    });

            log.info("department Id is fetching now : {}", dept);

            if (dept.getId() == currentUserDeptId) {     // Better to compare IDs instead of names
                Hall hall= hallRepository.findById(req.getHallId()).orElseThrow(() -> new RuntimeException("Hall not found for id: " + req.getHallId()));

                bookingDto dto = bookingDto.builder()
                        .username(user.getName())                    // ← From User
                        .hallName(hall.getHallName())                 // ← From BookingRequest
                        .date(req.getRequestedDate())                         // ← From BookingRequest
                        // Add more fields when you uncomment them in DTO
                        // .purpose(req.getPurpose())
                         .requestId(req.getId())
                        // .status(req.getStatus())
                        .build();

                dtoList.add(dto);
            }
        }

        if (dtoList.isEmpty()) {
            log.info("No pending requests found for this HOD");
            return ResponseEntity.ok(Collections.emptyList());   // Return empty list (better practice)
        }

        log.info("Found {} pending requests for HOD", dtoList.size());
        return ResponseEntity.ok(dtoList);
    }
    @PostMapping("/requestDetails/{requestId}")
    public ResponseEntity<requestInfoDto> getRequestInfo(@PathVariable long requestId) {
        return bookingService.getRequestInfo(requestId);
    }
    @PostMapping("/hod/accept/{requestId}")
    public ResponseEntity<?> acceptByHod(@PathVariable Long requestId) {
        log.info("HOD accept request with id: {}", requestId);
        return bookingService.acceptByHod(requestId);
    }

    @PostMapping("/hod/reject/{requestId}")
    public ResponseEntity<?> requestRejectedHOD(
            @PathVariable Long requestId) {

        return bookingService.rejectedByHOD(requestId);
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


}
