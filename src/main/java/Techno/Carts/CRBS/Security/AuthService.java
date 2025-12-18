package Techno.Carts.CRBS.Security;

import Techno.Carts.CRBS.Dto.LoginRequestDto;
import Techno.Carts.CRBS.Dto.LoginResponseDto;
import Techno.Carts.CRBS.Dto.SignupRequestDto;
import Techno.Carts.CRBS.Dto.SignupResponseDto;
import Techno.Carts.CRBS.Entity.Department;
import Techno.Carts.CRBS.Entity.Role;
import Techno.Carts.CRBS.Entity.User;
import Techno.Carts.CRBS.Repository.DepartmentRepository;
import Techno.Carts.CRBS.Repository.EmailDataSetRepository;
import Techno.Carts.CRBS.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final EmailDataSetRepository emailDataSetRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponseDto(token, user.getId());
    }


    public ResponseEntity<SignupResponseDto> signup(SignupRequestDto signupRequestDTO) {
        if (emailDataSetRepository.findByEmail(signupRequestDTO.getEmail()) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(userRepository.findByUsername(signupRequestDTO.getUsername()).isPresent()){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        }

        String email = signupRequestDTO.getEmail();
        String[] parts = email.split("\\.");

        if (parts.length < 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Department dept = departmentRepository
                .findByDepartmentName(parts[0])
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Long departmentId = dept.getId();
        User user = User.builder()
                .departmentId(departmentId)
                .role(Role.valueOf(parts[1].toUpperCase()))
                .name(parts[2])
                .email(email)
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))

                .build();


        userRepository.save(user);

        return ResponseEntity.of(Optional.of(SignupResponseDto.builder().id(user.getId()).username(user.getUsername()).build()));
    }
}
