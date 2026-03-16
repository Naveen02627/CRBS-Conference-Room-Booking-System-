//package Techno.Carts.CRBS.Security;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//
//@Getter
//@RequiredArgsConstructor
//public class CustomUserDetails implements UserDetails {
//
//    private final Long id;
//    private final String username;
//    private final String password;
//    private final String role;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        String finalRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
//        return List.of(new SimpleGrantedAuthority(finalRole));
//    }
//
//
//    @Override public boolean isAccountNonExpired() { return true; }
//    @Override public boolean isAccountNonLocked() { return true; }
//    @Override public boolean isCredentialsNonExpired() { return true; }
//    @Override public boolean isEnabled() { return true; }
//}
//
