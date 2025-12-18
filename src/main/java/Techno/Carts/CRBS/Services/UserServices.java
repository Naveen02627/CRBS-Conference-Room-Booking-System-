package Techno.Carts.CRBS.Services;

import Techno.Carts.CRBS.Entity.User;
import Techno.Carts.CRBS.Repository.DepartmentRepository;
import Techno.Carts.CRBS.Repository.EmailDataSetRepository;
import Techno.Carts.CRBS.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class UserServices {

    private final UserRepository userRepository;
    private final EmailDataSetRepository emailDataSetRepository;
    private final DepartmentRepository departmentRepository;


    public ResponseEntity<List<User>> getAllUser() {
        List<User> UserList = userRepository.findAll();
        if(UserList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(UserList));
    }
}
