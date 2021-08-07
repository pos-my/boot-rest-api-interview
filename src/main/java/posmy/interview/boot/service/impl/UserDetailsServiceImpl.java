package posmy.interview.boot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import posmy.interview.boot.entity.User;
import posmy.interview.boot.repository.IUserRepository;
import posmy.interview.boot.security.MyUserDetails;

public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private IUserRepository userRepository;
     
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
         
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
         
        return new MyUserDetails(user);
    }
}