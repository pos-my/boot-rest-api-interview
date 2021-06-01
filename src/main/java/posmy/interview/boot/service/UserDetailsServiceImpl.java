package posmy.interview.boot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import posmy.interview.boot.entity.User;
import posmy.interview.boot.model.MyUserDetails;
import posmy.interview.boot.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {
	 
    @Autowired
    private UserRepository userRepository;
     
    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
    	
        Optional<User> findUser = userRepository.findByUsername( username );
         
        if ( findUser.isEmpty() ) {
            throw new UsernameNotFoundException("Could not find user");
        }
        
        return new MyUserDetails( findUser.get() );
    }
 
}
