package Techno.Carts.CRBS.Services;


import Techno.Carts.CRBS.Entity.Hall;
import Techno.Carts.CRBS.Entity.HallAvailableDataBase;
import Techno.Carts.CRBS.Repository.HallAvailableDataBaseRepository;
import Techno.Carts.CRBS.Repository.HallRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class HallSearchService {

    private final HallAvailableDataBaseRepository hallAvailableDataBaseRepository;


    public List<String> returnCity(String state) {
        HallAvailableDataBase data = hallAvailableDataBaseRepository.findByState(state)
                .orElseThrow(() -> new RuntimeException("State not found"));
        return data.getCity();
    }

    public List<String> returnState(){
        List<HallAvailableDataBase> data = hallAvailableDataBaseRepository.findAll();

        List<String> state = new ArrayList<>();
        for (HallAvailableDataBase datum : data) {
            state.add(datum.getState());
        }
        return state;
    }
    public ResponseEntity<List<HallAvailableDataBase>> getLocation(){
        List<HallAvailableDataBase> data = hallAvailableDataBaseRepository.findAll();
        if(data.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        return ResponseEntity.of(Optional.of(data));
    }

}
