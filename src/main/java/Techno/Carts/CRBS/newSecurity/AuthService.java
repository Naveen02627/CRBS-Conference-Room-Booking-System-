package Techno.Carts.CRBS.newSecurity;

import Techno.Carts.CRBS.Dto.LoginRequestDto;
import Techno.Carts.CRBS.Dto.SignupRequestDto;
import Techno.Carts.CRBS.Entity.Department;
import Techno.Carts.CRBS.Entity.Role;
import Techno.Carts.CRBS.Entity.User;
import Techno.Carts.CRBS.Repository.DepartmentRepository;
import Techno.Carts.CRBS.Repository.EmailDataSetRepository;
import Techno.Carts.CRBS.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailDataSetRepository emailDataSetRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;


    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;




        public ResponseEntity<?> signup(SignupRequestDto signupRequestDTO) {// signupReqDTO == email,username,pass

        if (emailDataSetRepository.findByEmail(signupRequestDTO.getEmail()) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(userRepository.findByEmail(signupRequestDTO.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (userRepository.findByUsername(signupRequestDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String email = signupRequestDTO.getEmail();
        String[] parts = email.split("\\.");

        if (parts.length < 3) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Department dept = departmentRepository
                .findByDepartmentName(parts[0])
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = User.builder()
                .departmentId(dept.getId())
                .role(Role.valueOf(parts[1].toUpperCase()))
                .name(parts[2])
                .email(email)
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("Signup Successful");
    }

    public String verify(LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        if (!authentication.isAuthenticated()) {
            return "Not Authenticated";   // ← consider throwing exception instead
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Now call the instance method
        String token = jwtService.generateToken(user);

        return token;
    }
}
