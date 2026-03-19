package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Dto.LoginRequestDto;
import Techno.Carts.CRBS.Dto.LoginResponseDto;
import Techno.Carts.CRBS.Dto.SignupRequestDto;
import Techno.Carts.CRBS.Entity.User;
import Techno.Carts.CRBS.Services.CurrentUser;
import Techno.Carts.CRBS.newSecurity.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
//    private final JwtService jwtService;
    private final CurrentUser currentUser;
//
//
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {

    String token = authService.verify(request);

    if (token == null || token.equals("Not Authenticated")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
    }

    // ==================== COOKIE SETUP (This is what you want) ====================
    ResponseCookie jwtCookie = ResponseCookie.from("JWT_TOKEN", token)
            .path("/")                  // Very important
            .maxAge(60 * 60)            // 1 hour
            .httpOnly(true)
            .secure(true)               // Keep true in production (HTTPS)
            .sameSite("None")         // ← Change from "None" to "Strict" (recommended)
            // .sameSite("Lax")         // Use Lax only if Strict causes issues
            .build();

    response.addHeader("Set-Cookie", jwtCookie.toString());
    // ============================================================================

    // Optional: return nice JSON response
    return ResponseEntity.ok()
            .body(Map.of("message", "Login successful", "username", request.getUsername()));
}
@PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletResponse response) {

    ResponseCookie deleteCookie = ResponseCookie.from("JWT_TOKEN", "")
            .path("/")                  // Must be same as login
            .maxAge(0)                  // This deletes it
            .httpOnly(true)
            .secure(true)
            .sameSite("None")         // ← MUST match login exactly!
            .build();

    response.addHeader("Set-Cookie", deleteCookie.toString());

    return ResponseEntity.ok("Logged out successfully");
}
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignupRequestDto signupRequestDTO) {

        return authService.signup(signupRequestDTO);
    }

    @GetMapping("/getRole")
    public String getRole(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated user tried to access /getRole");
            return "Unauthorized";
        }

        try {
            String role = currentUser.getRole().toString();
            log.info("User role fetched: {}", role);
            return role;

        } catch (Exception e) {
            log.error("Error fetching user role", e);
            return "Error";
        }
    }


}
