package Techno.Carts.CRBS.Services;

import Techno.Carts.CRBS.Entity.Hall;
import Techno.Carts.CRBS.Entity.HallAvailableDataBase;
import Techno.Carts.CRBS.Entity.Location;
import Techno.Carts.CRBS.Repository.HallAvailableDataBaseRepository;
import Techno.Carts.CRBS.Repository.HallRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HallServices {

    private final HallRepository hallRepository;
    private final HallAvailableDataBaseRepository hallAvailableDataBaseRepository;

    public ResponseEntity<Hall> createNewHall(Hall hall){
         Hall newHall = Hall.builder()
                .hallName(hall.getHallName())
                .capacity(hall.getCapacity())
                .price(hall.getPrice())
                .location(Location.builder()
                        .state(hall.getLocation().getState())
                        .Address(hall.getLocation().getAddress())
                        .city(hall.getLocation().getCity())

                        .build())
                .bookedSlots(new HashMap<>())
                .build();
        Hall savedHall = hallRepository.insert(newHall);
        SaveToAvailHall(newHall.getLocation().getState(),newHall.getLocation().getCity());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHall);

    }
    public void SaveToAvailHall(String state, String city) {

        Optional<HallAvailableDataBase> optionalData =
                hallAvailableDataBaseRepository.findByState(state);

        if (optionalData.isEmpty()) {

            HallAvailableDataBase newLocation = new HallAvailableDataBase();
            newLocation.setState(state);

            List<String> cities = new ArrayList<>();
            cities.add(city);

            newLocation.setCity(cities);

            hallAvailableDataBaseRepository.save(newLocation);
        }
        else {

            HallAvailableDataBase existingLocation = optionalData.get();
            List<String> cities = existingLocation.getCity();


            if (!cities.contains(city)) {
                cities.add(city);
                existingLocation.setCity(cities);
                hallAvailableDataBaseRepository.save(existingLocation);
            }
        }
    }

    public ResponseEntity<List<Hall>> getAllHalls(){
        List<Hall> list = new ArrayList<>();
        list = hallRepository.findAll();
        if(list.size() <= 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.of(Optional.of(list));
    }
}
