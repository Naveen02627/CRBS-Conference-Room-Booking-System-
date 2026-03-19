package Techno.Carts.CRBS.Services;

import Techno.Carts.CRBS.Entity.Role;
import Techno.Carts.CRBS.Entity.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            return principal.getUserId();
        }
        throw new IllegalStateException("No authenticated user found");
    }

    public Role getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            return principal.getRole();
        }
        throw new IllegalStateException("No authenticated user found");
    }
    public long getDepartmentId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            Long deptId = userPrincipal.getDepartment();   // or however you named it
            if (deptId == null) {
                throw new IllegalStateException("DepartmentId is null in UserPrincipal");
            }
            return deptId;
        }

        throw new IllegalStateException("Principal is not UserPrincipal");
    }
}
