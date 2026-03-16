//package Techno.Carts.CRBS.Security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration configuration
//    ) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////
////        http
////                .csrf(csrf -> csrf.disable())
////                .sessionManagement(session ->
////                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                )
////                .authorizeHttpRequests(auth -> auth
////
////
////                        .requestMatchers("/auth/**")
////                        .permitAll()
////
////
//
////
////
////                        .requestMatchers("/booking/pendingRequestHOD","/search/**", "/hallList")
////                        .hasRole("HOD")
////
////
////                        .requestMatchers("/booking/pendingRequestADMIN", "/addHall")
////                        .hasRole("ADMIN")
////
////                        .anyRequest().authenticated()
////                )
////                .addFilterBefore(
////                        jwtAuthenticationFilter,
////                        UsernamePasswordAuthenticationFilter.class
////                );
////
////        return http.build();
////    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(Customizer.withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/login", "/auth/signup").permitAll()
//                        .requestMatchers("/auth/getRole").permitAll()
//                        .requestMatchers("/getLocation").permitAll()
//
//                        .requestMatchers("/search/**", "/booking/request").hasAnyRole("USER","ADMIN")
//
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(
//                        jwtAuthenticationFilter,
//                        UsernamePasswordAuthenticationFilter.class
//                );
//
//        return http.build();
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        // Allow the local front-end origins (use http scheme and correct port)
//        config.setAllowedOriginPatterns(List.of(
//                "https://localhost:5173",
//                "https://47.15.92.82:5173",
//                "http://localhost:5173",
//                "http://47.15.92.82:5173"
//        ));
//        // Permit common HTTP methods including OPTIONS for preflight
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        // Allow all headers (or list specific ones like Authorization, Content-Type, etc.)
//        config.setAllowedHeaders(List.of("*"));
//        // If cookies or credentials are needed, allow them
//        config.setAllowCredentials(true);
//
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//
//
//
//}
