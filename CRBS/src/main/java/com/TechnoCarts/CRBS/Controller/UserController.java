package com.TechnoCarts.CRBS.Controller;



import com.TechnoCarts.CRBS.Dto.UpdateUserRequestDTO;
import com.TechnoCarts.CRBS.Entity.User;
import com.TechnoCarts.CRBS.Services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserServices userServices;


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUser(){

        return userServices.getAllUser();
    }
    @PostMapping("/request")
    public ResponseEntity<?> UpdateReq(@RequestBody UpdateUserRequestDTO Dto){

        userServices.requestForProfileUpdate(Dto);
        return ResponseEntity.ok().build();
    }
}
