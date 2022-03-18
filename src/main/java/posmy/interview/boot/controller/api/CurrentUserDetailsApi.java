package posmy.interview.boot.controller.api;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import posmy.interview.boot.security.service.UserDetailsImpl;

public interface CurrentUserDetailsApi {
    default UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Please login again");
        } else {
            Object principal = authentication.getPrincipal();
            if (principal == null) {
                throw new AccessDeniedException("Please login again");
            } else if (principal instanceof String) {
                if (((String)principal).equalsIgnoreCase("anonymousUser")) {
                    throw new AccessDeniedException("Please login again");
                } else {
                    throw new AccessDeniedException("User authentication exception");
                }
            } else {
                try {
                    UserDetailsImpl userDetails = (UserDetailsImpl) principal;
                    return userDetails;
                } catch (Exception ex) {
                    throw new AccessDeniedException("Please login again");
                }
            }
        }
    }
}
