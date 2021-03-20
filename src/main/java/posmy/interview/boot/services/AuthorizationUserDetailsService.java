package posmy.interview.boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.UserAware;
import posmy.interview.boot.security.UserPrincipal;

import static posmy.interview.boot.security.AuthorityHelper.getAuthorities;

@Service
public class AuthorizationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Autowired
    private LoginService loginService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        UserAware authenticatedUser;
        String authorization = token.getCredentials().toString();

        if (authorization == null) {
            throw new BadCredentialsException("Invalid credential");
        }

        if (token.getCredentials() instanceof String) {
            authenticatedUser = loginService.login(authorization);
        } else {
            throw new UnsupportedOperationException("Not supported");
        }

        if (authenticatedUser == null) {
            throw new BadCredentialsException("Invalid credential");
        }

        return new UserPrincipal(authenticatedUser, getAuthorities(authenticatedUser));
    }

}
