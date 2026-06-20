package com.TechnoCarts.CRBS.Services;

import com.TechnoCarts.CRBS.Dto.UpdateUserRequestDTO;
import com.TechnoCarts.CRBS.Entity.Status;
import com.TechnoCarts.CRBS.Entity.User;
import com.TechnoCarts.CRBS.Entity.UserUpdateRequest;
import com.TechnoCarts.CRBS.HandleExaption.UserNotAuthorise;
import com.TechnoCarts.CRBS.HandleExaption.UserNotFoundException;
import com.TechnoCarts.CRBS.Repository.UserRepository;
import com.TechnoCarts.CRBS.Repository.UserUpdateRequestRepository;
import com.TechnoCarts.CRBS.Services.AdminService.UpdateUserDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserServices {

    private final CurrentUser currentUser;

    private final UserRepository userRepository;

    private final UserUpdateRequestRepository userUpdateRequestRepository;



    public ResponseEntity<List<User>> getAllUser() {
        List<User> UserList = userRepository.findAll();
        if(UserList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(UserList));
    }

    @Transactional
    public ResponseEntity<?> requestForProfileUpdate(UpdateUserRequestDTO dto) {

        Long currentUserId = currentUser.getUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));


        if (dto.getRole() == null && dto.getMobNumber() == null) {
            throw new RuntimeException(
                    "At least one field (Role or Mobile Number) must be provided");
        }

        UserUpdateRequest request = new UserUpdateRequest();


        request.setEmail(user.getEmail());


        if (dto.getRole() != null) {
            request.setRole(dto.getRole());
        }else{
            request.setRole(user.getRole());
        }

        if (dto.getMobNumber() != null) {
            request.setMobNumber(dto.getMobNumber());
        }else{
            request.setMobNumber(user.getMobNumber());
        }

        request.setStatus(Status.Requested);

        userUpdateRequestRepository.save(request);

        return ResponseEntity.ok("Profile update request submitted successfully");
    }
}
