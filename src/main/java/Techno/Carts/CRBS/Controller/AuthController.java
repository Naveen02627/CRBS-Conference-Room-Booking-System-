package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Dto.LoginRequestDto;
import Techno.Carts.CRBS.Dto.LoginResponseDto;
import Techno.Carts.CRBS.Dto.SignupRequestDto;
import Techno.Carts.CRBS.Entity.User;
import Techno.Carts.CRBS.Security.AuthService;
import Techno.Carts.CRBS.Security.CustomUserDetails;
import Techno.Carts.CRBS.Security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {

        User user = authService.authenticate(request);

        String token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", token)
                .httpOnly(true)
                .secure(true) // true for HTTPS (ngrok)
                .path("/")
                .maxAge(24 * 60 * 60) // 1 day
                .sameSite("None")     // required for cross-origin
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok("Login successful");

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(0); // 🔥 DELETE COOKIE

        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignupRequestDto signupRequestDTO) {

        return authService.signup(signupRequestDTO);
    }
    @GetMapping("/getRole")
    public ResponseEntity<String> getRole(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.ok("Not Authenticated");
        }

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        // role is already a String, no .name() needed
        String role = user.getRole();

        return ResponseEntity.ok(role);
    }


}
