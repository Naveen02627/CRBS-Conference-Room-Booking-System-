package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Entity.EmailDataSet;
import Techno.Carts.CRBS.Entity.User;
import Techno.Carts.CRBS.Services.ManageCandidateEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/candidate")
public class CandidateEmailManagerController {

    private final ManageCandidateEmail manageCandidateEmail;

    @PostMapping("/add")
    public ResponseEntity<EmailDataSet> addNewEmail(@RequestBody EmailDataSet request){
        return manageCandidateEmail.addNewEmail(request.getEmail());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EmailDataSet> deleteEmail(@PathVariable Long id){
        return manageCandidateEmail.deleteEmail(id);
    }
    @GetMapping("/all")
    public ResponseEntity<List<EmailDataSet>> showAllCandidate(){
        return manageCandidateEmail.getAll();

    }
//    @PostMapping("/addCandidate")
//    public ResponseEntity<?> addNewCandidate(@RequestBody User user){
//        return manageCandidateEmail.addCandidate(user);
//    }
}
