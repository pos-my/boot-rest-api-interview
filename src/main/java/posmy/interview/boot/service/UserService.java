package posmy.interview.boot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.exception.CustomNoDataFoundException;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<UserDto> getAllUsers() {

		List<User> users = userRepository.findAll();

		List<UserDto> userDtos = new ArrayList<>();
		users.forEach(user -> {
			UserDto dto = UserDto.builder().id(user.getId()).name(user.getName()).username(user.getUsername())
					.role(user.getRole()).enabled(user.isEnabled()).build();
			userDtos.add(dto);
		});

		return userDtos;
	}

	public UserDto getUserById(Long id) {
		User user = getUser(id);
		UserDto dto = UserDto.builder().id(user.getId()).name(user.getName()).username(user.getUsername()).role(user.getRole())
				.enabled(user.isEnabled()).build();

		return dto;
	}

	public UserDto getUserByUsername(String username) {
		User user = userRepository.findByUsername(username);

		if (user != null) {
			return UserDto.builder().id(user.getId()).name(user.getName()).username(user.getUsername())
					.role(user.getRole()).enabled(user.isEnabled()).build();
		}

		return null;
	}

	public void saveUser(UserDto userDto) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(userDto.getPassword());

		User user = User.builder().id(userDto.getId()).name(userDto.getName()).username(userDto.getUsername())
				.password(encodedPassword).enabled(true).role(userDto.getRole()).build();

		userRepository.save(user);
	}

	public void updateUser(UserDto userDto) {

		getUser(userDto.getId());
		saveUser(userDto);
	}

	public void deleteUserById(Long id) {

		getUser(id);
		userRepository.deleteById(id);
	}

	public void deleteAllUsers() {
		userRepository.deleteAll();
	}

	private User getUser(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new CustomNoDataFoundException("No user found."));
	}

}
