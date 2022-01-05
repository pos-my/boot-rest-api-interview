package posmy.interview.boot.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.constant.Constants;
import posmy.interview.boot.database.UserDao;
import posmy.interview.boot.model.database.UserEntity;
import posmy.interview.boot.util.Json;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userDao.findUserEntityByUserName(username);

        if (userEntity == null || userEntity.getStatus().equals(Constants.UserStatus.REMOVED.getType())) {
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetail(userEntity);
    }
}
