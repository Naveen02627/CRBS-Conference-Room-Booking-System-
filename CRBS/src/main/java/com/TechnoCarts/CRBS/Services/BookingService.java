package com.TechnoCarts.CRBS.Services;//package Techno.Carts.CRBS.Services;
//
//import Techno.Carts.CRBS.Entity.*;
//import Techno.Carts.CRBS.Repository.UserRepository;
//import jakarta.transaction.Transactional;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class BookingService {
//
//    private final BookingRequestRepository bookingRequestRepository;
//    private final UserRepository userRepository;
//    private final BookingHistoryRepository bookingHistoryRepository;
//    private final HallRepository hallRepository;
//    private final CurrentUser currentUser;
//
//    @Transactional
//    public Object requestToHOD(@Valid UserBookingRequestDto dto) {
//
//        log.info("RequestToHOD for Hall Id: "+dto.getHallId()+" on Date " +dto.getRequestedDate());
//
//        //Check Is this User Exist as user
//        Long userId = currentUser.getUserId();
//        User  user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        if(user.getRole() != Role.USER){
//            log.error("User is not a Authorized");
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED).ok().body("User not Authorized");
//        }
//        log.info("user Exist With userId : "+userId);
//
//        // Check Is already Requested
//
//        boolean request = isAlreadyRequested(dto, userId);
//
//        if(request){
//            return ResponseEntity.badRequest()
//                    .body("Already requested");
//        }
//        log.info("First request : ");
//        //Check Is Hall Is Available
//        Hall hall = hallRepository.findByHallId(dto.getHallId());
//        if(hall==null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST).ok().body(" Hall not found");
//        }
//        boolean checkAvailability = checkAvailability(hall,dto.getRequestedDate(),dto.getSlot());
//        if(!checkAvailability){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST).ok().body("Hall Not Available");
//        }
//        log.info("Requested Hall Id : "+dto.getHallId()+"Is available For Booking");
//        BookingRequest bookingRequest = BookingRequest.builder()
//                .hallId(dto.getHallId())
//                .purpose(dto.getPurpose())
//                .userId(userId)
//                .slotIds(dto.getSlot())
//                .requestedDate(dto.getRequestedDate())
//                .build();
//
//        Action action = Action.builder()
//                .actionById(userId)
//                .remark("")
//                .status(Status.REQUESTED)
//                .actionDateTime(LocalDateTime.now())
//                .bookingRequest(bookingRequest)
//                .build();
//
//        bookingRequest.setActions(List.of(action));
//
//        bookingRequestRepository.save(bookingRequest);
//
//        return new ResponseEntity<>(HttpStatus.CREATED).ok().body("Booking Request saved");
//
//    }
//
//    public boolean isAlreadyRequested(UserBookingRequestDto dto, Long userId) {
//        return bookingRequestRepository.isRequested(
//                dto.getHallId(),
//                dto.getRequestedDate(),
//                userId
//        );
//    }
//
//    public boolean checkAvailability(Hall hall, LocalDate requestedDate, List<Integer> slot) {
//        String date = requestedDate.toString();
//        Map<String, Set<Integer>> bookedSlots = hall.getBookedSlots();
//        if(!bookedSlots.containsKey(slot)) {
//            return true;
//        }
//        else{
//            Set<Integer> slots = bookedSlots.get(slot);
//            for(Integer slotId : slot) {
//                if(slots.contains(slotId)) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
////
////
////    public ResponseEntity<?> requestToHOD(UserBookingRequestDto dto) {
////
////        Long userId = currentUser.getUserId();
////        log.info("New booking request from userId: {}, hallId: {}, date: {}, slots: {}",
////                userId, dto.getHallId(), dto.getRequestedDate(), dto.getSlot());
////
////
////        // 1. Validate user exists (early exit)
////        if (!userRepository.existsById(userId)) {
////            log.error("Unauthorized booking attempt - userId {} not found", userId);
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
////                    .body("User not authenticated or does not exist");
////        }
////
////        // 2. Validate DTO (assume you have @Valid + annotations in DTO)
////        // If slot is null or empty → fail early
////        if (dto.getSlot() == null || dto.getSlot().isEmpty()) {
////            log.warn("Booking request with empty slots from userId: {}", userId);
////            return ResponseEntity.badRequest().body("At least one slot must be selected");
////        }
////
////        // 3. Build request entity
////        BookingRequest req = BookingRequest.builder()
////                .hallId(dto.getHallId())
////                .purpose(dto.getPurpose())
////                .requestedDate(dto.getRequestedDate())
////                .userId(userId)
////                .slotIds(new ArrayList<>(dto.getSlot()))  // safe copy
////                .status(Status.PENDING_HOD)
////                .build();
////
////        // 4. Check slot availability
////        if (!checkForAlreadyBooking(dto.getHallId(),
////                dto.getRequestedDate().toString(),
////                dto.getSlot(),
////                req)) {
////            // Slot conflict → already logged in check method
////            return ResponseEntity.badRequest().body("Selected slots are already booked");
////        }
////
////        // 5. Save (transactional)
////        bookingRequestRepository.save(req);
////        log.info("Booking request created successfully - requestId: {}, userId: {}",
////                req.getId(), userId);
////
////        return ResponseEntity.ok("Booking request submitted successfully to HOD");
////    }
////
////    // Your check method (with better logging & handling)
////    public boolean checkForAlreadyBooking(
////            String hallId,
////            String requestedDate,
////            List<Integer> requestedSlots,
////            BookingRequest req) {
////
////        log.debug("Checking slot availability for hall: {}, date: {}, requested slots: {}",
////                hallId, requestedDate, requestedSlots);
////
////        Hall hall = hallRepository.findById(hallId)
////                .orElseThrow(() -> {
////                    log.error("Hall not found: {}", hallId);
////                    return new RuntimeException("Hall does not exist");
////                });
////
////        Map<String, Set<Integer>> bookedSlots = hall.getBookedSlots();
////
////        Set<Integer> bookedOnDate = bookedSlots.getOrDefault(requestedDate, new HashSet<>());
////
////        for (Integer slot : requestedSlots) {
////            if (bookedOnDate.contains(slot)) {
////                log.warn("Slot conflict detected - slot: {}, date: {}, hall: {}",
////                        slot, requestedDate, hallId);
////
////                // Cancel / move to history
////                SaveToBookingHistory("CANCELLED",
////                        "Slots not available - conflict with existing booking",
////                        currentUser.getUserId(),
////                        req);
////
////                return false;
////            }
////        }
////
////        log.debug("All requested slots are available");
////        return true;
////    }
////
////    // Your history save method (already good, just minor logging improvement)
////    public void SaveToBookingHistory(String action, String remark, Long actionBy, BookingRequest request) {
////        log.info("Moving request {} to history - action: {}, remark: {}",
////                request.getId(), action, remark);
////
////        BookingHistory history = BookingHistory.builder()
////                .action(action)
////                .actionById(actionBy)
////                .remark(remark)
////                .userId(request.getUserId())
////                .purpose(request.getPurpose())
////                .requestedDate(request.getRequestedDate())
////                .hallId(request.getHallId())
////                .slots(new ArrayList<>(request.getSlotIds()))
////                .createdAt(LocalDate.now())
////                .build();
////
////        bookingHistoryRepository.save(history);
////        log.info("History entry created - historyId: {}", history.getId());
////
////        bookingRequestRepository.deleteById(request.getId());
////        log.info("Pending request {} deleted", request.getId());
////    }
////
//////*****************************************************************************//
////// not in use
//////    public ResponseEntity<List<BookingRequest>> HodPendingRequest() {
//////        List<BookingRequest> list = bookingRequestRepository.findByStatus(Status.PENDING_HOD);
//////
//////        if (list.isEmpty()) {
//////            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//////        }
//////        return ResponseEntity.of(Optional.of(list));
//////    }
////
////
////    //*******************************************************************************//
////    public ResponseEntity<?> acceptByHod(Long requestId) {
////        log.info("Accepting request with id: {}", requestId);
////
////        BookingRequest request = bookingRequestRepository.findById(requestId)
////                .orElseThrow(() -> {
////                    log.warn("Booking request with id: {} not found", requestId);
////                    return new RuntimeException("Booking request not found");
////                });
////
////        if (request.getStatus() != Status.PENDING_HOD) {
////            log.warn("Pending request with id: {} is Unknown", requestId);
////            throw new IllegalStateException("Request is not pending for HOD");
////        }
////
////        log.info("Accepted request with id: {}", requestId);
////        request.setStatus(Status.PENDING_ADMIN);
////        bookingRequestRepository.save(request);
////        log.info("Pending request with id: {} is now Pending For Admin", requestId);
////
////        return ResponseEntity.ok("Accepted Successfully");
////    }
////
////
//////********************************************************************************//
////    public ResponseEntity<?> rejectedByHOD(Long requestId) {
////        log.info("Rejected request with id: {}", requestId);
////        BookingRequest request = bookingRequestRepository.findById(requestId)
////                .orElseThrow(() -> new RuntimeException("Booking request not found"));
////
////        if (request.getStatus() != Status.PENDING_HOD) {
////            log.warn("request with id: {} is not for HOD", requestId);
////            throw new IllegalStateException("Request is not pending for HOD");
////        }
////
////        Long hodUserId = currentUser.getUserId();
////
////        request.setStatus(Status.REJECTED_BY_HOD);
////        bookingRequestRepository.save(request);
////        log.info("All Set For Rejection now Data Goes to BookingHistory: {}", requestId);
////
////        SaveToBookingHistory(
////                "REJECTED",
////                " ",
////                hodUserId,
////                request
////        );
////        log.info("Data is saved successfully - requestId: {} in Booking History", requestId);
////
////        return ResponseEntity.ok("Rejected Successfully");
////    }
////
//////***********************************************************************************//
////    @Transactional
////    public ResponseEntity<?> acceptedByAdmin(Long requestId) {
////        log.info("Accepted  request by Admin with id: {}", requestId);
////        Long adminId = currentUser.getUserId();
////
////        BookingRequest request = bookingRequestRepository.findById(requestId)
////                .orElseThrow(() -> new RuntimeException("Booking request not found"));
////
////        if (request.getStatus() != Status.PENDING_ADMIN) {
////            throw new IllegalStateException("Request is not pending for ADMIN");
////        }
////
////         request.setStatus(Status.APPROVED);
////        log.info(" All Set for request with id: {}", requestId);
////        SaveToBookingHistory(
////                "APPROVED",
////                "Done",
////                adminId,
////                request
////        );
////        log.info("request is saved in booking history and deleting from booking request : {}", requestId);
////        bookingRequestRepository.delete(request);
////
////        Hall hall = hallRepository.findById(request.getHallId())
////                .orElseThrow(() -> {
////                    log.warn("Hall not found with id: {}", request.getHallId());
////                    return new RuntimeException("Hall not found");
////                });
////
////
////        Map<String, Set<Integer>> bookedSlots = hall.getBookedSlots();
////        String dateKey = request.getRequestedDate().toString(); // or formatted date
////        Set<Integer> existingSlots = bookedSlots.getOrDefault(dateKey, new HashSet<>());
////        for (Integer slot : request.getSlotIds()) {
////            if (existingSlots.contains(slot)) {
////                throw new RuntimeException("Slot " + slot + " already booked for date " + dateKey);
////            }
////        }
////        existingSlots.addAll(request.getSlotIds());
////        bookedSlots.put(dateKey, existingSlots);
////        hall.setBookedSlots(bookedSlots);
////        hallRepository.save(hall);
////
////        return ResponseEntity.ok("Application Approved");
////    }
////    public ResponseEntity<?> rejectedByAdmin(Long requestId){
////        Long userId = currentUser.getUserId();
////
////        BookingRequest request = bookingRequestRepository.findById(requestId)
////                .orElseThrow(() -> {
////                    log.error("Booking request with id: {} not found", requestId);
////                    return new RuntimeException("Booking request not found");
////                });
////
////        if (request.getStatus() != Status.PENDING_ADMIN) {
////            log.error("request with id: {} is not for ADMIN", requestId);
////            throw new IllegalStateException("Request is not pending for ADMIN");
////        }
////        log.info("All Done For the request  Id{} Application has rejected ", requestId);
////        SaveToBookingHistory("REJECTED"," ",userId,request);
////
////        return ResponseEntity.ok("Application Rejected");
////    }
////    public ResponseEntity<List<bookingDto>> adminPendingRequest() {
////
////        List<BookingRequest> list = bookingRequestRepository.findByStatus(Status.PENDING_ADMIN);
////        List<bookingDto> dtoList = new ArrayList<>();
////
////        for (BookingRequest request : list) {
////
////            User user = userRepository.findById(request.getUserId())
////                    .orElseThrow(() -> new RuntimeException("User not found"));
////
////            Hall hall = hallRepository.findById(request.getHallId())
////                    .orElseThrow(() -> new RuntimeException("Hall not found"));
////
////            bookingDto dto = bookingDto.builder()
////                    .date(request.getRequestedDate())
////                    .requestId(request.getId())
////                    .username(user.getName())
////
////                    .hallName(hall.getHallName())
////                    .build();
////
////            dtoList.add(dto);
////        }
////
////        if (dtoList.isEmpty()) {
////            log.info("No admin pending requests found");
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
////        }
////
////        log.info("Admin pending requests are being shown");
////        return ResponseEntity.ok(dtoList); // ✅ CLEAN
////    }
////
////    public ResponseEntity<requestInfoDto> getRequestInfo(long requestId) {
////        BookingRequest req = bookingRequestRepository.findById(requestId)
////                .orElseThrow(() -> {
////                    log.error("Booking request with id: {} not found", requestId);
////                    return new RuntimeException("Booking request not found");
////                });
////        Hall hall = hallRepository.findById(req.getHallId()).orElseThrow(
////                () ->{
////                    log.error("Hall with id: {} not found", req.getHallId());
////                    return new RuntimeException("Hall with id: "+req.getHallId()+" not found");
////                });
////        requestInfoDto dto = requestInfoDto.builder()
////                .slots(req.getSlotIds())
////                .purpose(req.getPurpose())
////                .Address(hall.getLocation().getAddress())  // Note: address lowercase
////                .build();
////        return ResponseEntity.ok(dto);
////
////
////    }
////
////    public ResponseEntity<List<bookingDto>> fatchService() {
////
////        List<BookingHistory> list = bookingHistoryRepository.findAll();
////
////        if (list.isEmpty()) {
////            log.info("All Booking History are not available");
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
////        }
////
////        List<bookingDto> dtoList = new ArrayList<>();
////
////        for (BookingHistory request : list) {
////
////            User user = userRepository.findById(request.getUserId())
////                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
////
////            Hall hall = hallRepository.findById(request.getHallId())
////                    .orElseThrow(() -> new RuntimeException("Hall not found with id: " + request.getHallId()));
////
////            System.out.println("Hall ID from BookingHistory: " + request.getHallId());
////            System.out.println("Hall Name from MongoDB: " + hall.getHallName());
////
////            bookingDto dto = bookingDto.builder()
////                    .date(request.getRequestedDate())
////                    .requestId(request.getId())
////                    .username(user.getName())
////                    .hallName(hall.getHallName())
////                    .build();
////
////            dtoList.add(dto);
////        }
////
////        return ResponseEntity.ok(dtoList);
////    }
//////    private void addSlotToHall(BookingRequest bookingrequest) {
//////
//////        Hall hall = hallRepository.findById(bookingrequest.getHallId())
//////                .orElseThrow(() -> new RuntimeException("Hall does not exist"));
//////
//////        String key = bookingrequest.getRequestedDate().toString();
//////
//////        Map<String, Set<Integer>> bookedSlots = hall.getBookedSlots();
//////
//////        if (bookedSlots == null) {
//////            bookedSlots = new HashMap<>();
//////        }
//////
//////        bookedSlots.putIfAbsent(key, new HashSet<>());
//////
//////        Set<Integer> slots = bookedSlots.get(key);
//////
//////        if (slots.contains(bookingrequest.getSlotId())) {
//////            throw new IllegalArgumentException("Hall is already booked for this slot");
//////        }
//////
//////        slots.add(bookingrequest.getSlotId());
//////
//////        hall.setBookedSlots(bookedSlots);
//////        hallRepository.save(hall);
//////    }
//
//}
//
//
