package Techno.Carts.CRBS.Services;

import Techno.Carts.CRBS.Dto.UserBookingRequestDto;
import Techno.Carts.CRBS.Dto.requestInfoDto;
import Techno.Carts.CRBS.Entity.*;
import Techno.Carts.CRBS.Repository.BookingHistoryRepository;
import Techno.Carts.CRBS.Repository.BookingRequestRepository;
import Techno.Carts.CRBS.Repository.HallRepository;
import Techno.Carts.CRBS.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRequestRepository bookingRequestRepository;
    private final UserRepository userRepository;
    private final BookingHistoryRepository bookingHistoryRepository;
    private final HallRepository hallRepository;
    private final CurrentUser currentUser;

    @Transactional
    public ResponseEntity<?> requestToHOD(UserBookingRequestDto dto) {

        Long userId = currentUser.getUserId();
        log.info("New booking request from userId: {}, hallId: {}, date: {}, slots: {}",
                userId, dto.getHallId(), dto.getRequestedDate(), dto.getSlot());


        // 1. Validate user exists (early exit)
        if (!userRepository.existsById(userId)) {
            log.error("Unauthorized booking attempt - userId {} not found", userId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not authenticated or does not exist");
        }

        // 2. Validate DTO (assume you have @Valid + annotations in DTO)
        // If slot is null or empty → fail early
        if (dto.getSlot() == null || dto.getSlot().isEmpty()) {
            log.warn("Booking request with empty slots from userId: {}", userId);
            return ResponseEntity.badRequest().body("At least one slot must be selected");
        }

        // 3. Build request entity
        BookingRequest req = BookingRequest.builder()
                .hallId(dto.getHallId())
                .purpose(dto.getPurpose())
                .requestedDate(dto.getRequestedDate())
                .userId(userId)
                .slotIds(new ArrayList<>(dto.getSlot()))  // safe copy
                .status(Status.PENDING_HOD)
                .build();

        // 4. Check slot availability
        if (!checkForAlreadyBooking(dto.getHallId(),
                dto.getRequestedDate().toString(),
                dto.getSlot(),
                req)) {
            // Slot conflict → already logged in check method
            return ResponseEntity.badRequest().body("Selected slots are already booked");
        }

        // 5. Save (transactional)
        bookingRequestRepository.save(req);
        log.info("Booking request created successfully - requestId: {}, userId: {}",
                req.getId(), userId);

        return ResponseEntity.ok("Booking request submitted successfully to HOD");
    }

    // Your check method (with better logging & handling)
    public boolean checkForAlreadyBooking(
            String hallId,
            String requestedDate,
            List<Integer> requestedSlots,
            BookingRequest req) {

        log.debug("Checking slot availability for hall: {}, date: {}, requested slots: {}",
                hallId, requestedDate, requestedSlots);

        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> {
                    log.error("Hall not found: {}", hallId);
                    return new RuntimeException("Hall does not exist");
                });

        Map<String, Set<Integer>> bookedSlots = hall.getBookedSlots();

        Set<Integer> bookedOnDate = bookedSlots.getOrDefault(requestedDate, new HashSet<>());

        for (Integer slot : requestedSlots) {
            if (bookedOnDate.contains(slot)) {
                log.warn("Slot conflict detected - slot: {}, date: {}, hall: {}",
                        slot, requestedDate, hallId);

                // Cancel / move to history
                SaveToBookingHistory("CANCELLED",
                        "Slots not available - conflict with existing booking",
                        currentUser.getUserId(),
                        req);

                return false;
            }
        }

        log.debug("All requested slots are available");
        return true;
    }

    // Your history save method (already good, just minor logging improvement)
    public void SaveToBookingHistory(String action, String remark, Long actionBy, BookingRequest request) {
        log.info("Moving request {} to history - action: {}, remark: {}",
                request.getId(), action, remark);

        BookingHistory history = BookingHistory.builder()
                .action(action)
                .actionById(actionBy)
                .remark(remark)
                .userId(request.getUserId())
                .purpose(request.getPurpose())
                .requestedDate(request.getRequestedDate())
                .slots(new ArrayList<>(request.getSlotIds()))
                .createdAt(LocalDate.now())
                .build();

        bookingHistoryRepository.save(history);
        log.info("History entry created - historyId: {}", history.getId());

        bookingRequestRepository.deleteById(request.getId());
        log.info("Pending request {} deleted", request.getId());
    }

//*****************************************************************************//
// not in use
//    public ResponseEntity<List<BookingRequest>> HodPendingRequest() {
//        List<BookingRequest> list = bookingRequestRepository.findByStatus(Status.PENDING_HOD);
//
//        if (list.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.of(Optional.of(list));
//    }


    //*******************************************************************************//
    public ResponseEntity<?> acceptByHod(Long requestId) {
        log.info("Accepting request with id: {}", requestId);

        BookingRequest request = bookingRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.warn("Booking request with id: {} not found", requestId);
                    return new RuntimeException("Booking request not found");
                });

        if (request.getStatus() != Status.PENDING_HOD) {
            log.warn("Pending request with id: {} is Unknown", requestId);
            throw new IllegalStateException("Request is not pending for HOD");
        }

        log.info("Accepted request with id: {}", requestId);
        request.setStatus(Status.PENDING_ADMIN);
        bookingRequestRepository.save(request);
        log.info("Pending request with id: {} is now Pending For Admin", requestId);

        return ResponseEntity.ok("Accepted Successfully");
    }


//********************************************************************************//
    public ResponseEntity<?> rejectedByHOD(Long requestId) {
        log.info("Rejected request with id: {}", requestId);
        BookingRequest request = bookingRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Booking request not found"));

        if (request.getStatus() != Status.PENDING_HOD) {
            log.warn("request with id: {} is not for HOD", requestId);
            throw new IllegalStateException("Request is not pending for HOD");
        }

        Long hodUserId = currentUser.getUserId();

        request.setStatus(Status.REJECTED_BY_HOD);
        bookingRequestRepository.save(request);
        log.info("All Set For Rejection now Data Goes to BookingHistory: {}", requestId);

        SaveToBookingHistory(
                "REJECTED",
                " ",
                hodUserId,
                request
        );
        log.info("Data is saved successfully - requestId: {} in Booking History", requestId);

        return ResponseEntity.ok("Rejected Successfully");
    }

//***********************************************************************************//
    @Transactional
    public ResponseEntity<?> acceptedByAdmin(Long requestId) {
        log.info("Accepted  request by Admin with id: {}", requestId);
        Long adminId = currentUser.getUserId();

        BookingRequest request = bookingRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Booking request not found"));

        if (request.getStatus() != Status.PENDING_ADMIN) {
            throw new IllegalStateException("Request is not pending for ADMIN");
        }

//        // Check availability FIRST
//            SaveToBookingHistory(
//                    "REJECTED",
//                    "Hall is not available for this slot",
//                    adminId,
//                    request
//            );
//            bookingRequestRepository.delete(request);
//            throw new IllegalStateException("Hall is not available");
//        }

        // Approved
         request.setStatus(Status.APPROVED);
        log.info(" All Set for request with id: {}", requestId);
        SaveToBookingHistory(
                "APPROVED",
                "Done",
                adminId,
                request
        );
        log.info("request is saved in booking history and deleting from booking request : {}", requestId);
        bookingRequestRepository.delete(request);

        return ResponseEntity.ok("Application Approved");
    }
    public ResponseEntity<?> rejectedByAdmin(Long requestId,String remark){
        Long userId = currentUser.getUserId();

        BookingRequest request = bookingRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Booking request with id: {} not found", requestId);
                    return new RuntimeException("Booking request not found");
                });

        if (request.getStatus() != Status.PENDING_ADMIN) {
            log.error("request with id: {} is not for ADMIN", requestId);
            throw new IllegalStateException("Request is not pending for ADMIN");
        }
        log.info("All Done For the request  Id{} Application has rejected ", requestId);
        SaveToBookingHistory("REJECTED",remark,userId,request);

        return ResponseEntity.ok("Application Rejected");
    }
    public ResponseEntity<List<BookingRequest>> AdminPendingRequest(){
        List<BookingRequest> list =  bookingRequestRepository.findByStatus(Status.PENDING_ADMIN);

        if(list.isEmpty()){
            log.info("Admin pending requests are not available");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Admin pending requests are  showing now");
        return ResponseEntity.of(Optional.of(list));
    }

    public ResponseEntity<requestInfoDto> getRequestInfo(long requestId) {
        BookingRequest req = bookingRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Booking request with id: {} not found", requestId);
                    return new RuntimeException("Booking request not found");
                });
        Hall hall = hallRepository.findById(req.getHallId()).orElseThrow(
                () ->{
                    log.error("Hall with id: {} not found", req.getHallId());
                    return new RuntimeException("Hall with id: "+req.getHallId()+" not found");
                });
        requestInfoDto dto = requestInfoDto.builder()
                .slots(req.getSlotIds())
                .purpose(req.getPurpose())
                .Address(hall.getLocation().getAddress())  // Note: address lowercase
                .build();
        return ResponseEntity.ok(dto);


    }
//    private void addSlotToHall(BookingRequest bookingrequest) {
//
//        Hall hall = hallRepository.findById(bookingrequest.getHallId())
//                .orElseThrow(() -> new RuntimeException("Hall does not exist"));
//
//        String key = bookingrequest.getRequestedDate().toString();
//
//        Map<String, Set<Integer>> bookedSlots = hall.getBookedSlots();
//
//        if (bookedSlots == null) {
//            bookedSlots = new HashMap<>();
//        }
//
//        bookedSlots.putIfAbsent(key, new HashSet<>());
//
//        Set<Integer> slots = bookedSlots.get(key);
//
//        if (slots.contains(bookingrequest.getSlotId())) {
//            throw new IllegalArgumentException("Hall is already booked for this slot");
//        }
//
//        slots.add(bookingrequest.getSlotId());
//
//        hall.setBookedSlots(bookedSlots);
//        hallRepository.save(hall);
//    }

}


