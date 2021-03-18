package posmy.interview.boot.entity;

import java.io.Serializable;

public interface JpaEntity<ID extends Serializable> extends IEntity<ID> {
}
