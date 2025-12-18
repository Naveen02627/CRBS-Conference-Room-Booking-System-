package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Entity.Hall;
import Techno.Carts.CRBS.Entity.HallAvailableDataBase;
import Techno.Carts.CRBS.Services.HallSearchService;
import Techno.Carts.CRBS.Services.HallServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class HallController {


    private final HallServices hallServices;
    private final HallSearchService hallSearchService;

    @PostMapping("/addHall")
    public ResponseEntity<Hall> addNewHall(@RequestBody Hall hall){

        return hallServices.createNewHall(hall);
    }

    @GetMapping("/hallList")
    public ResponseEntity<List<Hall>> GetAllHalls(){

        return hallServices.getAllHalls();
    }
    @GetMapping("/GetCity/{state}")
    public List<String> ReturnHalls(@PathVariable String state){

        return hallSearchService.returnCity(state);
    }
    @GetMapping("/getState")
    public List<String> returnState(){

        return hallSearchService.returnState();
    }
    @GetMapping("/getLocation")
    public ResponseEntity<List<HallAvailableDataBase>> getLocation(){

        return hallSearchService.getLocation();
    }


}
