package com.TechnoCarts.CRBS.Controller;


import com.TechnoCarts.CRBS.Dto.UpdateHall;
import com.TechnoCarts.CRBS.Dto.UpdateUserRequestDTO;
import com.TechnoCarts.CRBS.Dto.hallDTO;
import com.TechnoCarts.CRBS.Services.AdminService.ShowBookingRequest;
import com.TechnoCarts.CRBS.Services.AdminService.UpdateUserDetails;
import com.TechnoCarts.CRBS.Services.AdminService.hallManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final UpdateUserDetails updateUserDetails;
    private final hallManager hallManager;
    private final ShowBookingRequest showBookingRequest;


    @PostMapping("/addHall")
    public ResponseEntity<?> CreateHall(@RequestBody hallDTO Dto){
        log.info("Creating : "+Dto.getHallName()+"In process");
        hallManager.AddNawHall(Dto);
        log.info("Done ✔️");
        return ResponseEntity.status(HttpStatus.OK).body(Dto);
    }

    @PutMapping("/updateHall")
    public ResponseEntity<?> updateHall(@RequestBody UpdateHall updateHall){

        hallManager.updateHall(updateHall);

        return  ResponseEntity.status(HttpStatus.OK).body(hallManager.updateHall(updateHall));
    }
    @GetMapping("/getAllRequest")
    public ResponseEntity<?> getAllRequest() {
        return updateUserDetails.RequestForUserDetailsUpdate();
    }

    @PutMapping("/acceptUpdate")
    public ResponseEntity<?> acceptUpdate(@RequestBody Long id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateUserDetails.AcceptUserRequest(id));
    }

    @PutMapping("/declingUpdate")
    public ResponseEntity<?> declingUpdate(@RequestBody Long id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateUserDetails.declineRequest(id));
    }

    @GetMapping("/getAllBookingRequest")
    public ResponseEntity<?> getAllBookingRequest(){
        return  ResponseEntity.status(HttpStatus.OK).body(showBookingRequest.ShowRequest());
    }

}
