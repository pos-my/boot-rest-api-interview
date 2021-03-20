package posmy.interview.boot.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import posmy.interview.boot.entity.UserAware;

import java.util.*;

public class AuthorityHelper {

    // for testing purpose
    private static List<SimpleGrantedAuthority> librarianAuthorities = Collections.unmodifiableList(Arrays.asList(
            new SimpleGrantedAuthority("BOOK_CREATE"),
            new SimpleGrantedAuthority("BOOK_READ"),
            new SimpleGrantedAuthority("BOOK_UPDATE"),
            new SimpleGrantedAuthority("BOOK_DELETE"),
            new SimpleGrantedAuthority("MEMBER_CREATE"),
            new SimpleGrantedAuthority("MEMBER_READ"),
            new SimpleGrantedAuthority("MEMBER_UPDATE"),
            new SimpleGrantedAuthority("MEMBER_DELETE"))
    );

    private static List<SimpleGrantedAuthority> memberAuthorities = Collections.unmodifiableList(Arrays.asList(
            new SimpleGrantedAuthority("BOOK_READ"),
            new SimpleGrantedAuthority("BOOK_BORROW"),
            new SimpleGrantedAuthority("BOOK_RETURN"),
            new SimpleGrantedAuthority("SELF_DELETE"))
    );

    public static Collection<? extends GrantedAuthority> getAuthorities(UserAware user) {
        if (user.isLibrarian()) {
            return librarianAuthorities;
        } else if (user.isMember()) {
            return memberAuthorities;
        }
        // no authorities
        return new ArrayList<>();
    }
}
