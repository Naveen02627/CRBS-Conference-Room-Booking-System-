package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Dto.LoginRequestDto;
import Techno.Carts.CRBS.Dto.LoginResponseDto;
import Techno.Carts.CRBS.Dto.SignupRequestDto;
import Techno.Carts.CRBS.Dto.SignupResponseDto;
import Techno.Carts.CRBS.Security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto) {

        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDTO){
        return authService.signup(signupRequestDTO);
    }
}
