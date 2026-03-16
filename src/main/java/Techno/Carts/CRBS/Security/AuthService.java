//package Techno.Carts.CRBS.Security;
//
//import Techno.Carts.CRBS.Dto.LoginRequestDto;
//import Techno.Carts.CRBS.Dto.SignupRequestDto;
//import Techno.Carts.CRBS.Entity.Department;
//import Techno.Carts.CRBS.Entity.Role;
//import Techno.Carts.CRBS.Entity.User;
//import Techno.Carts.CRBS.Repository.DepartmentRepository;
//import Techno.Carts.CRBS.Repository.EmailDataSetRepository;
//import Techno.Carts.CRBS.Repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final EmailDataSetRepository emailDataSetRepository;
//    private final UserRepository userRepository;
//    private final DepartmentRepository departmentRepository;
//    private final PasswordEncoder passwordEncoder;
//
//
//    public User authenticate(LoginRequestDto request) {
//
//
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.UNAUTHORIZED
//                ));
//        if (!passwordEncoder.matches(
//                request.getPassword(),
//                user.getPassword())) {
//            throw new ResponseStatusException(
//                    HttpStatus.UNAUTHORIZED,
//                    "Invalid password"
//            );
//        }
//
//        return user;
//    }
//
//
//    public ResponseEntity<?> signup(SignupRequestDto signupRequestDTO) {
//
//        if (emailDataSetRepository.findByEmail(signupRequestDTO.getEmail()) == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        if(userRepository.findByEmail(signupRequestDTO.getEmail()).isPresent()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if (userRepository.findByUsername(signupRequestDTO.getUsername()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        String email = signupRequestDTO.getEmail();
//        String[] parts = email.split("\\.");
//
//        if (parts.length < 3) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        Department dept = departmentRepository
//                .findByDepartmentName(parts[0])
//                .orElseThrow(() -> new RuntimeException("Department not found"));
//
//        User user = User.builder()
//                .departmentId(dept.getId())
//                .role(Role.valueOf(parts[1].toUpperCase()))
//                .name(parts[2])
//                .email(email)
//                .username(signupRequestDTO.getUsername())
//                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
//                .build();
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok("Signup Successful");
//    }
//}
