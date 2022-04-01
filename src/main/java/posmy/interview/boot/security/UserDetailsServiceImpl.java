package posmy.interview.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import posmy.interview.boot.repositories.UserRepository;
import posmy.interview.boot.entities.User;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = new User();
        try {
            user = userRepo.findByUsername(username);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }

        return new UserAuthentication(user);
    }
}