package com.TechnoCarts.CRBS.Services.BookingService;

import com.TechnoCarts.CRBS.Entity.BookingRequest;
import com.TechnoCarts.CRBS.Entity.Hall;
import com.TechnoCarts.CRBS.Entity.Status;
import com.TechnoCarts.CRBS.Entity.pair;
import com.TechnoCarts.CRBS.HandleExaption.BookingNotFoundError;
import com.TechnoCarts.CRBS.HandleExaption.BookingRequestError;
import com.TechnoCarts.CRBS.HandleExaption.HallNotFound;
import com.TechnoCarts.CRBS.Repository.BookingRequestRepository;
import com.TechnoCarts.CRBS.Repository.HallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingResponse {
    private final BookingRequestRepository bookingRequestRepository;
    private final HallRepository hallRepository;
    public ResponseEntity<?> adminAccept(Long id){

        Optional<BookingRequest> bookingRequest = bookingRequestRepository.findById(id);

        if(!bookingRequest.isPresent()) {
          throw  new BookingNotFoundError("Booking Id : "+id+" is Not Found");
        }
        Long HallId = bookingRequest.get().getHallId();

        Optional<Hall> hall = hallRepository.findById(HallId);

        if(!hall.isPresent()) {
            throw  new HallNotFound("Hall Id : "+id+" is Not Found");
        }
        LocalDate date = bookingRequest.get().getRequestedDate();

        ConcurrentHashMap<LocalDate, pair[]> slots = hall.get().getSlots();
        pair[] p = slots.get(date);

        boolean flag = false;
        List<Integer> list  = bookingRequest.get().getSlots();
        for(Integer i : list){
            if(p[i].getBooking() == 1){
                flag = true;
                break;
            }else{
                p[i].setBooking(1);
                p[i].setReqCnt(0);
            }
        }
        if(flag){
            throw new BookingRequestError("Booking Id : "+id+" is Already Booked");
        }
        slots.put(date, p);
        hall.get().setSlots(slots);
        hallRepository.save(hall.get());
        bookingRequest.get().setStatus(Status.Accepted);
        BookingRequest req =  bookingRequestRepository.save(bookingRequest.get());

        return ResponseEntity.status(HttpStatus.OK).body(req);
    }
    public ResponseEntity<?> adminReject(Long id){
        Optional<BookingRequest> bookingRequest = bookingRequestRepository.findById(id);
        if(!bookingRequest.isPresent()) {
            throw  new BookingNotFoundError("Booking Id : "+id+" is Not Found");
        }
        bookingRequest.get().setStatus(Status.Cancelled);
        BookingRequest req =  bookingRequestRepository.save(bookingRequest.get());
        return ResponseEntity.status(HttpStatus.OK).body(req);



    }
}
