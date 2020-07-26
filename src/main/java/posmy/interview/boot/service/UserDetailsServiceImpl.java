package posmy.interview.boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.security.MyUserDetails;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;
     
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        List<User> users = userRepository.findAll();
        
        Optional<User> user = users.stream().filter(u -> username.equalsIgnoreCase(u.getUsername())).findFirst();
         
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Could not find user");
        }
         
        return new MyUserDetails(user.get());
    }

}
