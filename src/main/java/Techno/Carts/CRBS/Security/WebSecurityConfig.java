package Techno.Carts.CRBS.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        //  AUTH – PUBLIC
                        .requestMatchers("/auth/**").permitAll()

                        //  USER
                        .requestMatchers(
                                "/search/**",
                                "/booking/request"
                        ).hasRole("USER")

                        //  HOD
                        .requestMatchers(
                                "/booking/pendingRequestHOD",
                                "/booking/HODAccept",
                                "/booking/HODReject"
                        ).hasRole("HOD")

                        //  ADMIN
                        .requestMatchers(
                                "/booking/pendingRequestADMIN",
                                "/booking/ADMINAccept",
                                "/booking/ADMINReject",

                                "/addHall",
                                "/hallList",
                                "/GetCity/**",
                                "/getState",
                                "/getLocation",

                                "/department/**",
                                "/candidate/**",
                                "/user/**"
                        ).hasRole("ADMIN")


                        .anyRequest().authenticated()
                )


                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // for testing

//@Bean
//public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//    http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                    .anyRequest().permitAll()
//            );
//
//    return http.build();
//}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
