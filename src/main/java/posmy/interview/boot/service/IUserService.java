package posmy.interview.boot.service;

import java.util.List;
import java.util.Optional;

import posmy.interview.boot.entity.User;
import posmy.interview.boot.object.UserObject;

public interface IUserService {
	Optional<User> create(UserObject userObject);
	
	Optional<User> update(UserObject userObject, Long userId);
	
	Optional<Boolean> delete(Long userId);
	
	Optional<List<User>> findAll();
	
	Optional<User> findByUserId(Long userId);
	
	Optional<User> findByUsernameAndPassword(String username, String password);
	
	Optional<User> findByUuid(String uuid);
}
