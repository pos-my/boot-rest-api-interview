package posmy.interview.boot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import posmy.interview.boot.model.SystemUser;
import posmy.interview.boot.model.UserDetail;
import posmy.interview.boot.respository.UserDetailRepository;

@Service
@Slf4j
public class SystemUserService implements UserDetailsService{

	private UserDetailRepository userDetailRepository;
	
	public SystemUserService(UserDetailRepository userDetailRepository) {
		this.userDetailRepository = userDetailRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserDetail> user = userDetailRepository.findByUsername(username);
		
		if(user.isEmpty()) {
			
			throw new UsernameNotFoundException(username + " not found!");
		}
		
		log.info(user.toString());
		
		return user.map(SystemUser::new).get();
	}

}
