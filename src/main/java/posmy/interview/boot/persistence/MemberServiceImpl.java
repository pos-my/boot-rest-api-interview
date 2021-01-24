package posmy.interview.boot.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
public class MemberServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Member member = repository.findByName(name);
        if (member != null) {
            return new User(member.getName(), member.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name())));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + name);
        }
    }
}
