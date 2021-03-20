package posmy.interview.boot.mockauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import posmy.interview.boot.entity.UserAware;
import posmy.interview.boot.mockauth.WithMockCustomUser;
import posmy.interview.boot.security.UserPrincipal;
import posmy.interview.boot.services.AuthorizationUserDetailsService;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Autowired
    private AuthorizationUserDetailsService authorizationUserDetailsService;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserAware userAware;
        if (customUser.isLibrarian()) {
            userAware = LibrarianBuilder.sample()
                    .withId(customUser.id())
                    .withUsername(customUser.username())
                    .build();
        } else {
            userAware = MemberBuilder.sample()
                    .withId(customUser.id())
                    .build();
        }

        Set<SimpleGrantedAuthority> authorities = Arrays.stream(customUser.authorities())
                .map(s -> new SimpleGrantedAuthority(s))
                .collect(Collectors.toSet());

        UserPrincipal userPrincipal = new UserPrincipal(userAware, authorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, "password", userPrincipal.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }
}
