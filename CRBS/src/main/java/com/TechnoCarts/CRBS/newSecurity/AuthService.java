package com.TechnoCarts.CRBS.newSecurity;


import com.TechnoCarts.CRBS.Dto.LoginRequestDto;
import com.TechnoCarts.CRBS.Dto.SignupRequestDto;
import com.TechnoCarts.CRBS.Entity.Role;

import com.TechnoCarts.CRBS.Entity.User;
import com.TechnoCarts.CRBS.HandleExaption.EmailAlreadyInUseException;
import com.TechnoCarts.CRBS.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;





    public ResponseEntity<?> signup(SignupRequestDto signupRequestDTO){

        String collegeEmail = signupRequestDTO.getEmail();
        Optional<User> user = userRepository.findByEmail(collegeEmail);
        if (user.isPresent()) {
            throw new EmailAlreadyInUseException(
                    "This Email is already in use: " + collegeEmail + " use another"
            );
        }

        User newUser = User.builder()
                .name(signupRequestDTO.getName())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .email(signupRequestDTO.getEmail())
                .role(Role.Student)
                .username(signupRequestDTO.getUsername())
                .build();
        userRepository.save(newUser);
        return ResponseEntity.ok("Signup");
    }

    public String verify(LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        if (!authentication.isAuthenticated()) {
            return "Not Authenticated";
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));


        String token = jwtService.generateToken(user);

        return token;
    }
}
