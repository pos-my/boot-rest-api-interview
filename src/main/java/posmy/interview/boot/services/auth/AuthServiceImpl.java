package posmy.interview.boot.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.db.UserDal;
import posmy.interview.boot.model.auth.UserAuthentication;
import posmy.interview.boot.model.entity.UserEntity;

@Service
public class AuthServiceImpl implements UserDetailsService {

    @Autowired
    private UserDal userDal;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userDal.findByUserName(username);

        if(null == user){
            throw new UsernameNotFoundException("User not found!");
        }

        return new UserAuthentication(user);
    }
}
