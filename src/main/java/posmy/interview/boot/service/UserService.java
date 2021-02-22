package posmy.interview.boot.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import posmy.interview.boot.entity.User;
import posmy.interview.boot.model.UserInfo;
import posmy.interview.boot.model.requestModel.FirstTimeRegisterRequest;
import posmy.interview.boot.model.requestModel.RegisterRequest;

public interface UserService {

	List<User> findAll();
	User findOne(String username);
	UserDetails loadUserByUsername(String username);
	void saveRegister(RegisterRequest signUpRequest);
	void saveFirstRegister(FirstTimeRegisterRequest signUpRequest);
}
