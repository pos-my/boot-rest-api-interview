package posmy.interview.boot.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.domain.User;
import posmy.interview.boot.enums.UserStatus;
import posmy.interview.boot.repo.UserRepository;
import org.springframework.security.core.userdetails.User.UserBuilder;

import java.util.Optional;

@Service
public class AuthUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("username is empty");
        }

        Optional<User> validUser = userRepository.findUserByUsername(username);
        UserBuilder builder = null;
        if (validUser.isPresent()) {
            User user = validUser.get();
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(user.getPassword());
            builder.roles(user.getRole().toString());
            builder.disabled(user.getStatus().compareTo(UserStatus.DELETED) == 0);
            return builder.build();
        }
        throw new UsernameNotFoundException(username + "is not found");
    }
}