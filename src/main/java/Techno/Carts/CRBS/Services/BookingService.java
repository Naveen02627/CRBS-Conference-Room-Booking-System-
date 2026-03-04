package Techno.Carts.CRBS.Services;

import Techno.Carts.CRBS.Dto.UserBookingRequestDto;
import Techno.Carts.CRBS.Entity.*;
import Techno.Carts.CRBS.Repository.BookingHistoryRepository;
import Techno.Carts.CRBS.Repository.BookingRequestRepository;
import Techno.Carts.CRBS.Repository.HallRepository;
import Techno.Carts.CRBS.Repository.UserRepository;
import Techno.Carts.CRBS.Security.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRequestRepository bookingRequestRepository;
    private final UserRepository userRepository;
    private final BookingHistoryRepository bookingHistoryRepository;
    private final HallRepository hallRepository;



    public ResponseEntity<?> requestToHOD(UserBookingRequestDto userBookingRequestDto) {

        Long userId = SecurityUtil.getCurrentUserId();

        if(!userRepository.existsById(userId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        for(int i = 0;i < userBookingRequestDto.getSlot().size();i++) {
            if (!checkForAlreadyBooking(userBookingRequestDto.getHallId(), userBookingRequestDto.getRequestedDate().toString(), userBookingRequestDto.getSlot().get(i))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        BookingRequest createRequest = BookingRequest.builder()
                .hallId(userBookingRequestDto.getHallId())
                .purpose(userBookingRequestDto.getPurpose())
                .requestedDate(userBookingRequestDto.getRequestedDate())
                .userId(userId)
                .status(Status.PENDING_HOD)

                .build();
        Optional<User> user = userRepository.findById(userId);

//        System.out.println("User Name is" + user);
//
//        bookingRequestRepository.save(createRequest);

        return ResponseEntity.ok("Successful requested");

    }
    //*****************************************************************************//

    public ResponseEntity<List<BookingRequest>> HodPendingRequest(){
        List<BookingRequest> list =  bookingRequestRepository.findByStatus(Status.PENDING_HOD);

        if(list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(list));

    }
    //*******************************************************************************//
    public ResponseEntity<?> acceptByHod(Long requestId) {

        BookingRequest request = bookingRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Booking request not found"));

        if (request.getStatus() != Status.PENDING_HOD) {
            throw new IllegalStateException("Request is not pending for HOD");
        }

        request.setStatus(Status.PENDING_ADMIN);
        bookingRequestRepository.save(request);

        return ResponseEntity.ok("Accepted Successfully");
    }


    //********************************************************************************//
//    public ResponseEntity<?> rejectedByHOD(Long requestId, String remark) {
//
//        BookingRequest request = bookingRequestRepository.findById(requestId)
//                .orElseThrow(() -> new RuntimeException("Booking request not found"));
//
//        if (request.getStatus() != Status.PENDING_HOD) {
//            throw new IllegalStateException("Request is not pending for HOD");
//        }
//
//        Long hodUserId = SecurityUtil.getCurrentUserId();
//
//        request.setStatus(Status.REJECTED_BY_HOD);
//        bookingRequestRepository.save(request);
//
//        SaveToBookingHistory(
//                "REJECTED",
//                remark,
//                hodUserId,
//                request
//        );
//
//        return ResponseEntity.ok("Rejected Successfully");
//    }

    //***********************************************************************************//
//    @Transactional
//    public ResponseEntity<?> acceptedByAdmin(Long requestId) {
//
//        Long adminId = SecurityUtil.getCurrentUserId();
//
//        BookingRequest request = bookingRequestRepository.findById(requestId)
//                .orElseThrow(() -> new RuntimeException("Booking request not found"));
//
//        if (request.getStatus() != Status.PENDING_ADMIN) {
//            throw new IllegalStateException("Request is not pending for ADMIN");
//        }
//
//        // Check availability FIRST
//        boolean available = checkForAlreadyBooking(
//                request.getHallId(),
//                request.getRequestedDate().toString(),
//                request.getSlotId()
//        );
//
//        if (!available) {
//            SaveToBookingHistory(
//                    "REJECTED",
//                    "Hall is not available for this slot",
//                    adminId,
//                    request
//            );
//            bookingRequestRepository.delete(request);
//            throw new IllegalStateException("Hall is not available");
//        }
//
//        // Approved
//        request.setStatus(Status.APPROVED);
//
//        SaveToBookingHistory(
//                "APPROVED",
//                "",
//                adminId,
//                request
//        );
//
//        addSlotToHall(request);
//
//        bookingRequestRepository.delete(request);
//        return ResponseEntity.ok("Application Approved");
//    }
//    public ResponseEntity<?> rejectedByAdmin(Long requestId,String remark){
//        Long userId = SecurityUtil.getCurrentUserId();
//
//        BookingRequest request = bookingRequestRepository.findById(requestId)
//                .orElseThrow(() -> new RuntimeException("Booking request not found"));
//
//        if (request.getStatus() != Status.PENDING_ADMIN) {
//            throw new IllegalStateException("Request is not pending for ADMIN");
//        }
//        SaveToBookingHistory("REJECTED",remark,userId,request);
//
//        return ResponseEntity.ok("Application Rejected");
//    }
//    public ResponseEntity<List<BookingRequest>> AdminPendingRequest(){
//        List<BookingRequest> list =  bookingRequestRepository.findByStatus(Status.PENDING_ADMIN);
//
//        if(list.isEmpty()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.of(Optional.of(list));
//    }
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


    public boolean checkForAlreadyBooking(
            String hallId,
            String requestedDate,
            int slot) {

        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new RuntimeException("Hall does not exist"));

        Map<String, Set<Integer>> bookedSlots = hall.getBookedSlots();

        // No bookings yet → available
        if (bookedSlots == null) {
            return true;
        }

        // No booking on this date → available
        if (!bookedSlots.containsKey(requestedDate)) {
            return true;
        }

        // Slot already exists?
        return !bookedSlots.get(requestedDate).contains(slot);
    }

//    public void SaveToBookingHistory(String action,String remark,Long ActionBy,BookingRequest request){
//        bookingHistoryRepository.save(BookingHistory.builder()
//                .action(action)
//                .actionById(ActionBy)
//                .remark(remark)
//                .purpose(request.getPurpose())
//                .UserId(request.getUserId())
//                .requestedDate(request.getRequestedDate())
//                .slot(request.getSlotId())
//                .CreatedAt(request.getCreatedAt())
//                .build());
//
//        bookingRequestRepository.deleteById(request.getId());
//    }

}
