package posmy.interview.boot.service;

import java.util.List;

import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.User;

public interface UserService {

	public List<User> getAllUser();

	public User updateUser(User userDto);

	public User deleteUser(Long userid, String username);

	public User deleteUser(String name);

	public User addUser(User user);
    
}
