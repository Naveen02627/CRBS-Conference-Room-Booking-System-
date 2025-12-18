package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Entity.User;
import Techno.Carts.CRBS.Services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserServices userServices;


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUser(){
        return userServices.getAllUser();
    }
}
