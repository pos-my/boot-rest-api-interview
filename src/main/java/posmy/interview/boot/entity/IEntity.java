package posmy.interview.boot.entity;

import java.io.Serializable;

public interface IEntity<ID extends Serializable> extends Serializable {

    ID getId();
}
