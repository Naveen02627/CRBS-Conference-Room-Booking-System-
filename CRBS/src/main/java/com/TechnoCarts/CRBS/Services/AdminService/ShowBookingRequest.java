package com.TechnoCarts.CRBS.Services.AdminService;

import com.TechnoCarts.CRBS.Dto.RequestBookingDto;
import com.TechnoCarts.CRBS.Entity.BookingRequest;
import com.TechnoCarts.CRBS.Entity.Status;
import com.TechnoCarts.CRBS.Repository.BookingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShowBookingRequest {

    private final BookingRequestRepository  bookingRequestRepository;
    public ResponseEntity<List<RequestBookingDto>> ShowRequest() {
        log.info("ShowRequest");
        List<BookingRequest> list = bookingRequestRepository.findRequestWhereStatusIsRequested(Status.Requested);
        log.info("list size is {} ",list.size()+" !");
        if(list.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<RequestBookingDto> dtoList = new ArrayList<>();

        for (BookingRequest req : list) {

            RequestBookingDto dto = new RequestBookingDto();

            dto.setRequestedDate(req.getRequestedDate());
            dto.setPurpose(req.getPurpose());
            dto.setHallId(req.getHallId());
            dto.setSlots(req.getSlots());

            dtoList.add(dto);
        }
        log.info("dto : {}",(Object) dtoList);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
