package Techno.Carts.CRBS.Services;

import Techno.Carts.CRBS.Entity.EmailDataSet;
import Techno.Carts.CRBS.Entity.User;
import Techno.Carts.CRBS.Repository.EmailDataSetRepository;
import Techno.Carts.CRBS.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManageCandidateEmail {

    private final EmailDataSetRepository emailDataSetRepository;
    private final UserRepository userRepository;

    public ResponseEntity<EmailDataSet> addNewEmail(String email) {

        if (emailDataSetRepository.findByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        }

        EmailDataSet newEmail = new EmailDataSet();
        newEmail.setEmail(email);

        emailDataSetRepository.save(newEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<EmailDataSet> deleteEmail(Long id) {

        if (!emailDataSetRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        emailDataSetRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<List<EmailDataSet>> getAll() {
        List<EmailDataSet> list = emailDataSetRepository.findAll();
        if(list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(list));
    }
//    public ResponseEntity<?> addCandidate(User user) {
//        userRepository.save(user);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
}
