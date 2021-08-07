package posmy.interview.boot.security;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import posmy.interview.boot.entity.User;
import posmy.interview.boot.service.IUserService;
@Component
public class UUIDAuthenticationService  implements UserAuthenticationService{
	@Autowired
	IUserService userService;
	
	@Override
	public Optional<String> login(String username, String password) {
		Optional<User> user = userService.findByUsernameAndPassword(username, password);
		return Optional.of(user.get().getUuid());
	}

	@Override
	public Optional<User> findByToken(String token) {
		// TODO Auto-generated method stub
		Optional<User> user = userService.findByUuid(token);
		return user;
	}

	@Override
	public void logout(User user) {
		// TODO Auto-generated method stub
		
	}

}
