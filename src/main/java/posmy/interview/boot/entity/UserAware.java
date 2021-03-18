package posmy.interview.boot.entity;

import posmy.interview.boot.security.AccessTokenAware;

import java.io.Serializable;

public interface UserAware<E extends Serializable> extends IEntity<E>, AccessTokenAware {

    boolean isMember();

    boolean isLibrarian();

    String getName();

}
