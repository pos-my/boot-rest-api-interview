package posmy.interview.boot.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import posmy.interview.boot.entity.User;
import posmy.interview.boot.object.UserObject;
import posmy.interview.boot.repository.IRoleRepository;
import posmy.interview.boot.repository.IUserRepository;
import posmy.interview.boot.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	IUserRepository userRepo;

	@Autowired
	IRoleRepository roleRepo;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public Optional<User> create(UserObject userObject) {
		User userEntity = new User();
		userEntity.setFullName(userObject.getFullName());
		userEntity.setUsername(userObject.getUsername());
		userEntity.setPassword(this.passwordEncoder().encode(userObject.getPassword()));
		userEntity.setRole(roleRepo.getById(userObject.getRole()));
		userEntity.setUuid(UUID.randomUUID().toString());

		userRepo.save(userEntity);

		return Optional.of(userEntity);
	}

	@Override
	public Optional<User> update(UserObject userObject, Long userId) {
		Optional<User> userOptional = userRepo.findById(userId);

		User userEntity = userOptional.get();
		if(!userObject.getFullName().isBlank())
			userEntity.setFullName(userObject.getFullName());
		if(!userObject.getUsername().isBlank())
			userEntity.setUsername(userObject.getUsername());
		if(userObject.getPassword() != null && !userObject.getPassword().isBlank())
			userEntity.setPassword(userObject.getPassword());
		if(userObject.getRole() != null && userObject.getRole() != 0)
			userEntity.setRole(roleRepo.getById(userObject.getRole()));
		userRepo.save(userEntity);
		return Optional.of(userEntity);
	}

	@Override
	public Optional<Boolean> delete(Long userId) {
		User userEntity = new User();
		Optional<User> tempUser = this.findByUserId(userId);

		userEntity = tempUser.get();
		userEntity.setDeleted(true);

		userRepo.save(userEntity);

		return Optional.of(userEntity.isDeleted());
	}

	@Override
	public Optional<List<User>> findAll() {
		Optional<List<User>> userList = Optional.ofNullable(userRepo.findAll());

		return userList;
	}

	@Override
	public Optional<User> findByUserId(Long userId) {
		Optional<User> optionalUser = userRepo.findById(userId);

		return optionalUser;
	}

	@Override
	public Optional<User> findByUsernameAndPassword(String username, String password) {
		User optionalUser = userRepo.findByUsername(username);
		if (!this.passwordEncoder().matches(password, optionalUser.getPassword()) == true) {
			optionalUser = null;
		}

		return Optional.of(optionalUser);
	}

	@Override
	public Optional<User> findByUuid(String uuid) {
		User optionalUser = userRepo.findByUuid(uuid);
		return Optional.of(optionalUser);
	}

}
