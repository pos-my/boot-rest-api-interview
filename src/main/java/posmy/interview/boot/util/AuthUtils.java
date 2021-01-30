package posmy.interview.boot.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    private AuthUtils () {}

    public static String getUsername () {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
