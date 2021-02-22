package posmy.interview.boot.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import posmy.interview.boot.dao.RoleDao;
import posmy.interview.boot.dao.UserDao;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.model.RoleConst;
import posmy.interview.boot.model.UserInfo;
import posmy.interview.boot.model.requestModel.FirstTimeRegisterRequest;
import posmy.interview.boot.model.requestModel.RegisterRequest;
import posmy.interview.boot.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	PasswordEncoder encoder;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);

		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return UserInfo.build(user);
	}

	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		userDao.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public User findOne(String username) {
		return userDao.findByUsername(username);
	}


	@Override
	public void saveRegister(RegisterRequest signUpRequest) {
		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleDao.findByName(RoleConst.ROLE_LIBRARIAN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "librarian":
					Role libRole = roleDao.findByName(RoleConst.ROLE_LIBRARIAN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(libRole);

					break;
				case "member":
					Role memRole = roleDao.findByName(RoleConst.ROLE_MEMBER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(memRole);

					break;
				default:
					Role userRole = roleDao.findByName(RoleConst.ROLE_MEMBER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userDao.save(user);
	}

	@Override
	public void saveFirstRegister(FirstTimeRegisterRequest signUpRequest) {

		User user = new User(signUpRequest.getUsername(), 
				encoder.encode(signUpRequest.getPassword()));
		Set<Role> roles = new HashSet<>();
		Role userRole = roleDao.findByName(RoleConst.ROLE_LIBRARIAN)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);



		user.setRoles(roles);
		userDao.save(user);
	}
}
