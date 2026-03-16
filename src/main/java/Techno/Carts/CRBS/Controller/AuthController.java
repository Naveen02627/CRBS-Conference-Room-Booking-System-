package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Dto.LoginRequestDto;
import Techno.Carts.CRBS.Dto.LoginResponseDto;
import Techno.Carts.CRBS.Dto.SignupRequestDto;
import Techno.Carts.CRBS.Entity.User;
import Techno.Carts.CRBS.newSecurity.AuthService;
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
//    private final JwtService jwtService;
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
            .httpOnly(true)           // ← prevents XSS
            .secure(false)            // ← set true in production (HTTPS)
            .path("/")                // available everywhere
            .maxAge(60 * 60)          // 1 hour (better than 10 hours or 24h)
            .sameSite("Lax")          // "Strict" is safer, "None" only if cross-origin
            .build();

    response.addHeader("Set-Cookie", jwtCookie.toString());
    // ============================================================================

    // Optional: return nice JSON response
    return ResponseEntity.ok()
            .body(Map.of("message", "Login successful", "username", request.getUsername()));
}
//
@PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletResponse response) {

    ResponseCookie deleteCookie = ResponseCookie.from("JWT_TOKEN", "")
            .path("/")
            .maxAge(0)
            .build();

    response.addHeader("Set-Cookie", deleteCookie.toString());

    return ResponseEntity.ok("Logged out successfully");
}
//
//
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignupRequestDto signupRequestDTO) {

        return authService.signup(signupRequestDTO);
    }
//    @GetMapping("/getRole")
//    public ResponseEntity<String> getRole(Authentication authentication) {
//
//        if (authentication == null || !authentication.isAuthenticated()
//                || authentication.getPrincipal().equals("anonymousUser")) {
//            return ResponseEntity.ok("Not Authenticated");
//        }
//
//        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
//
//        // role is already a String, no .name() needed
//        String role = user.getRole();
//
//        return ResponseEntity.ok(role);
//    }


}
