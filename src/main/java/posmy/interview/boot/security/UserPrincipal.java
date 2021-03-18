package posmy.interview.boot.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import posmy.interview.boot.entity.UserAware;

import java.util.Collection;

public class UserPrincipal extends User {

    private UserAware user;

    public UserPrincipal(UserAware user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getName(), "", authorities);
        this.user = user;
    }

    public UserAware getUser() {
        return this.user;
    }
}
