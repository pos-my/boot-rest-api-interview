package posmy.interview.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import posmy.interview.boot.entities.User;
import posmy.interview.boot.repositories.UserRepository;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentSession {
    private static final Logger logger = LogManager.getLogger(CurrentSession.class);

    private User user;

    @Autowired
    private UserRepository userRepository;

    synchronized protected User getCurrentUser() {
        if (user == null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
                user = userRepository.findByUsername(username);
            }
        }
        return user;
    }
}
