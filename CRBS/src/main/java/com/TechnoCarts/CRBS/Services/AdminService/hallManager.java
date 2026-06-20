package com.TechnoCarts.CRBS.Services.AdminService;


import com.TechnoCarts.CRBS.Dto.UpdateHall;
import com.TechnoCarts.CRBS.Dto.hallDTO;
import com.TechnoCarts.CRBS.Entity.Hall;
import com.TechnoCarts.CRBS.Repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class hallManager {

    private final HallRepository hallRepository;

    public ResponseEntity<?> AddNawHall(hallDTO hallDTO) {

        Optional<Hall> hall = hallRepository.findByHallName(hallDTO.getHallName());

        if(hall.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User Another Hall Name");
        }
        Hall newHall = Hall.builder()
                .ImageUrl("https://www.wework.com/ideas/wp-content/uploads/sites/4/2021/08/20201008-199WaterSt-2_v1-scaled.jpg?fit=2048%2C1152")
                .name(hallDTO.getHallName())
                .details(hallDTO.getDetails())
                .location(hallDTO.getLocation())
                .isActive(true)

                .build();
        hallRepository.save(newHall);
        return  ResponseEntity.status(HttpStatus.CREATED).body(newHall);
    }

    public ResponseEntity<?> updateHall(UpdateHall dto) {

        Optional<Hall> hallOptional = hallRepository.findByHallName(dto.getName());

        if (hallOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Hall Not Found");
        }

        Hall hall = hallOptional.get();
        hall.setDetails(dto.getDetails());

        hall.setImageUrl(dto.getImageUrl());
        hall.setName(dto.getName());
        hall.setActive(dto.isActive());

        Hall updatedHall = hallRepository.save(hall);

        return ResponseEntity.ok(updatedHall);
    }

}
