package com.TechnoCarts.CRBS.Services.BookingService;

import com.TechnoCarts.CRBS.Dto.HallResponse;
import com.TechnoCarts.CRBS.Entity.Hall;
import com.TechnoCarts.CRBS.Entity.pair;
import com.TechnoCarts.CRBS.HandleExaption.HallNotFound;
import com.TechnoCarts.CRBS.Repository.HallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@Service
public class CheckAvailability {

    private final HallRepository hallRepository;

    public ResponseEntity<List<HallResponse>> getHall(LocalDate date){
        log.info("in Service getHall for date " +date);

        List<Hall> list = hallRepository.findAll();
        log.info("total halls : {}",list.size());
        List<HallResponse> listDTO = new ArrayList<>();
        for(Hall hall : list){
            ConcurrentHashMap<LocalDate , pair[]> slots = new ConcurrentHashMap<>();
            if(hall.getSlots() == null){
                log.info("hall slots is null");
                pair[] pairs = new pair[6];
                Arrays.fill(pairs ,new pair(0,0));
                slots.put(date,pairs);
            }else{
                slots =  hall.getSlots();
            }

            if(!slots.containsKey(date) ){
                pair[] pairs = new pair[6];
                Arrays.fill(pairs, new pair(0,0));
               HallResponse dto =  HallResponse.builder()
                       .slotsBooked(pairs)
                       .hallName(hall.getName())
                       .Details(hall.getDetails())
                       .ImageUrl(hall.getImageUrl())
                       .build();
               listDTO.add(dto);
               slots.put(date, pairs);
            }else {
                pair[] pairs = slots.get(date);
                for (pair it : pairs) {
                    if (it.getBooking() == 0) {
                        HallResponse dto = HallResponse.builder()
                                .slotsBooked(pairs)
                                .hallName(hall.getName())
                                .Details(hall.getDetails())
                                .ImageUrl(hall.getImageUrl())
                                .build();
                        listDTO.add(dto);
                        break;
                    }
                }

            }

        }
        log.info("filter all halls now : {}",listDTO.size());

        if (listDTO.isEmpty()) {
            throw new HallNotFound("No hall available on " + date);
        }


        return ResponseEntity.status(HttpStatus.OK).body(listDTO);

    }

}
