package posmy.interview.boot.security;

import java.util.Optional;

import org.springframework.stereotype.Component;

import posmy.interview.boot.entity.User;
@Component
public interface UserAuthenticationService {

	  Optional<String> login(String username, String password);


	  Optional<User> findByToken(String token);


	  void logout(User user);
}
