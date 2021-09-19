package posmy.interview.boot.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import posmy.interview.boot.exception.CustomException;
import posmy.interview.boot.exception.ResourceAlreadyExists;
import posmy.interview.boot.exception.ResourceNotFoundException;
import posmy.interview.boot.model.AccStatus;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService  {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User addUser(User user) {
		Optional<User> tmpUser = userRepository.findByUsername(user.getUsername());
		User updatedUser = null;
		if(!tmpUser.isPresent()) {
			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			user.setAccStatus(AccStatus.ACTIVE);
			updatedUser=userRepository.save(user);
		}else {
			throw new ResourceAlreadyExists("user already exists");
		}
		return updatedUser;
	}
	
	@Override
	public User updateUser(User user) {
		Optional<User> tmpUser = userRepository.findByUsername(user.getUsername());
		User updatedUser = null;
		
		if(tmpUser.isPresent()) {
			User existUser = tmpUser.get();
			existUser.setPassword(user.getPassword());
			existUser.setRole(user.getRole());
			if(!existUser.getAccStatus().equals(user.getAccStatus())) {
				existUser.setAccStatus(user.getAccStatus());
			}
			updatedUser=userRepository.save(existUser);
		} else {
			throw new ResourceNotFoundException("user not exists");
		}
		return updatedUser;
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}
	
	@Override
	public User deleteUser(Long userid, String username) {
		Optional<User> tmpUser = userRepository.findById(userid);
		Optional<User> curUser = userRepository.findByUsername(username);
		User updatedUser = null;
		if(curUser.isPresent() && tmpUser.isPresent()) {
			User currentUser=curUser.get();
			User existUser = tmpUser.get();
			if(existUser.getRole().equals(Role.MEMBER) && existUser.getAccStatus().equals(AccStatus.ACTIVE) 
					&& existUser.getBook()==null)
			{
				existUser.setAccStatus(AccStatus.DELETE);
				updatedUser=userRepository.save(existUser);
			} else {
					throw new CustomException("cannot be deleted because of the user role or having book not returned");
			}
		} else {
			throw new ResourceNotFoundException("id not exist");
		}
		return updatedUser;
	}
	
	@Override
	public User deleteUser(String username) {
		Optional<User> curUser = userRepository.findByUsername(username);
		User updatedUser = null;
		User currentUser =null;
		if(curUser.isPresent()) {
			currentUser=curUser.get();
			if(currentUser.getAccStatus().equals(AccStatus.ACTIVE) && currentUser.getBook()==null) {
				currentUser.setAccStatus(AccStatus.DELETE);
				updatedUser=userRepository.save(currentUser);
			}else {
				throw new CustomException("you are still having book not returned");
			}
		}	
		return updatedUser;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    if ( username == null || username.isEmpty() ){
	        throw new UsernameNotFoundException("username is empty");
	    }

	    Optional<User> validUser = userRepository.findByUsername(username);
	    UserBuilder builder = null;
	    if( validUser.isPresent()){
	    	User user = validUser.get();
	        builder = org.springframework.security.core.userdetails.User.withUsername(username);
	        builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
	        builder.roles(user.getRole().toString());
	        builder.disabled(user.getAccStatus().compareTo(AccStatus.DELETE)==0?true:false);
	        return builder.build();
	    }
	    throw new UsernameNotFoundException( username + "is not found");
	}

}
