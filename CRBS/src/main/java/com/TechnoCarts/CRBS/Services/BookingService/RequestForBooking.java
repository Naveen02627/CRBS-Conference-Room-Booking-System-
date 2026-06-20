package com.TechnoCarts.CRBS.Services.BookingService;

import com.TechnoCarts.CRBS.Dto.HallResponse;
import com.TechnoCarts.CRBS.Dto.RequestBookingDto;
import com.TechnoCarts.CRBS.Entity.*;
import com.TechnoCarts.CRBS.HandleExaption.BookingRequestError;
import com.TechnoCarts.CRBS.HandleExaption.HallNotFound;
import com.TechnoCarts.CRBS.HandleExaption.UserNotAuthorise;
import com.TechnoCarts.CRBS.HandleExaption.UserNotFoundException;
import com.TechnoCarts.CRBS.Repository.BookingRequestRepository;
import com.TechnoCarts.CRBS.Repository.HallRepository;
import com.TechnoCarts.CRBS.Repository.UserRepository;
import com.TechnoCarts.CRBS.Services.CurrentUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@RequiredArgsConstructor
@Service
@Slf4j
public class RequestForBooking {

    private final BookingRequestRepository bookingRequestRepository;
    private final UserRepository userRepository;
    private final HallRepository hallRepository;
    private final CurrentUser currentUser;

    @Transactional
    public ResponseEntity<?> CreateRequest(RequestBookingDto dto ){
        log.info("Creating request for booking Now in Service layer");
        Long currentUserId = currentUser.getUserId();
        log.info("Current User Id: " + currentUserId);
        Optional<User> currentUser = userRepository.findById(currentUserId);

        log.info("Current User: {}", currentUser.get().getName());
        //Check User Details
        if(!currentUser.isPresent()){
            throw new UserNotFoundException("User not found");
        }
        // Check is this User Is Authorise
        log.info("Current User Has Role : {}", currentUser.get().getRole());
        if(currentUser.get().getRole().equals("Student")){
            throw new UserNotAuthorise("User Name :"+currentUser.get().getName()+" Is not groupRepresentative");
        }

        // Check is Hall Available
        Hall hall = hallRepository.findById(dto.getHallId()).orElseThrow(
                () -> new HallNotFound("Hall not found "+dto.getHallId())
        );

        log.info("Hall : {}", hall.getName()+"is Available");

        ConcurrentHashMap<LocalDate, pair[]> map = new ConcurrentHashMap<>();

        pair[] p = new pair[6];
        LocalDate date = dto.getRequestedDate();

        log.info("Date : {}", date);
        if(!map.containsKey(date) ) {
            log.info("key is not present so we are adding new one {}",date);
            Arrays.fill(p, new pair(0, 0));
        }else{
            log.info("key is present so we are adding new one {}",date);
            p = map.get(date);
        }

        log.info("this is over pair {}", (Object) p);

        List<Integer> slots = dto.getSlots();
        boolean flag = false;
        int m = 0;
        for(Integer s : slots){
            if(p[s].getBooking() == 1){
                flag = true;
                m = s;
                break;
            }else{
                p[s].setReqCnt(p[s].getReqCnt()+1);
            }
        }

        if(flag){
            log.info("slot {} has already Booked",m);
            throw new BookingRequestError("Slot :"+m+" Has Already Booked");

        }
        map.put(date,p);
        hall.setSlots(map);
        hallRepository.save(hall);

        BookingRequest bookingRequest = BookingRequest.builder()
                .requestedAt(LocalDateTime.now())
                .hallId(dto.getHallId())
                .requestedDate(dto.getRequestedDate())
                .slots(slots)
                .userId(currentUserId)
                .status(Status.Requested)
                .purpose(dto.getPurpose())
                .build();

        bookingRequestRepository.save(bookingRequest);
        return ResponseEntity.ok().body(bookingRequest);

    }
}
