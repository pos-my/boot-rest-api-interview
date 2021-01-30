package posmy.interview.boot.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.RolesEntity;
import posmy.interview.boot.entity.UsersEntity;
import posmy.interview.boot.repository.UsersRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Authenticate {}", username);
        UsersEntity usersEntity = usersRepository.findByUsername(username);
        log.info("User {}", usersEntity);
        if(usersEntity == null){
            throw new UsernameNotFoundException("Could not find user");
        }
        return new User(usersEntity.getUsername(), usersEntity.getPassword(), getAuthorities(usersEntity.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<RolesEntity> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<RolesEntity> roles) {
        List<String> privileges = new ArrayList<>();
        for (RolesEntity role : roles) {
            privileges.add(role.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
