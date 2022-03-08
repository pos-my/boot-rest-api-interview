package posmy.interview.boot.repository;

import org.springframework.data.repository.CrudRepository;
import posmy.interview.boot.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserName(String userName);
}
