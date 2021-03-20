package posmy.interview.boot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import posmy.interview.boot.entity.UserAware;
import posmy.interview.boot.security.IAuthenticationFacade;
import posmy.interview.boot.security.UserPrincipal;

public abstract class BaseService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    protected UserAware getAuthenticatedUser() {
        final Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication != null) {
            final Object principalObj = authentication.getPrincipal();

            if (principalObj == null) {
                throw new IllegalStateException("Principal is unexpectedly undefined!");
            }

            if (principalObj instanceof UserPrincipal) {
                return ((UserPrincipal) principalObj).getUser();
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(
                            "Unsupported principal type '{}'",
                            principalObj
                                    .getClass()
                                    .getName());
                }
            }
        }
        return null;
    }
}
