package com.TechnoCarts.CRBS.Services.AdminService;


import com.TechnoCarts.CRBS.Dto.UpdateUserRequestDTO;
import com.TechnoCarts.CRBS.Entity.Status;
import com.TechnoCarts.CRBS.Entity.User;
import com.TechnoCarts.CRBS.Entity.UserUpdateRequest;
import com.TechnoCarts.CRBS.HandleExaption.UserNotFoundException;
import com.TechnoCarts.CRBS.Repository.UserRepository;
import com.TechnoCarts.CRBS.Repository.UserUpdateRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UpdateUserDetails {

    private final UserRepository userRepository;

    private final UserUpdateRequestRepository userUpdateRequestRepository;

    @Transactional
    public ResponseEntity<?> updateUser(UpdateUserRequestDTO updateUserRequest) {
        log.info("Updating User Details of email : ", updateUserRequest.getEmail());
        User user = userRepository.findByEmail(updateUserRequest.getEmail()).orElseThrow(
                ()-> new UserNotFoundException("Email Id : "+updateUserRequest.getEmail()+" not Found")
        );
        user.setRole(updateUserRequest.getRole());
        user.setMobNumber(updateUserRequest.getMobNumber());

        userRepository.save(user);
        log.info("Updated Completed : ", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    public ResponseEntity<?> RequestForUserDetailsUpdate() {

        List<UserUpdateRequest> userUpdateRequests = userUpdateRequestRepository.findAllByStatus(Status.Requested);

        if(userUpdateRequests.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return  new ResponseEntity<>(userUpdateRequests, HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> AcceptUserRequest(Long id) {
        UserUpdateRequest req = userUpdateRequestRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Request Id : "+id+" not Found")
        );
        log.info("Accepted User Request{} : ", req.getEmail());
        UpdateUserRequestDTO  Dto = new UpdateUserRequestDTO();

        Dto.setEmail(req.getEmail());
        Dto.setRole(req.getRole());
        Dto.setMobNumber(req.getMobNumber());

        userUpdateRequestRepository.deleteById(id);
        log.info("User Updated : {}", req);
        return updateUser(Dto);

    }
    public ResponseEntity<?> declineRequest(Long id) {
        UserUpdateRequest req = userUpdateRequestRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Request Id : "+id+" not Found")
        );
        log.info("Declined User Request {}: ", req.getEmail());
        userUpdateRequestRepository.deleteById(id);
        log.info("User Declined : {}", req);
        return new ResponseEntity<>(req, HttpStatus.OK);
    }

}
