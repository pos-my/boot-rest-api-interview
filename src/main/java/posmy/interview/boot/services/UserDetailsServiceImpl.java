package posmy.interview.boot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.domains.Users;
import posmy.interview.boot.repositories.UserRepository;

@Service("userDetailsServiceImpl")
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("Loaded {}", user.getUserName());
        return new UserDetailsImpl(user);
    }
}
