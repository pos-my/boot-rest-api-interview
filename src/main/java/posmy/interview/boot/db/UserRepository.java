package posmy.interview.boot.db;

import org.springframework.data.repository.CrudRepository;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;
import posmy.interview.boot.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String userName);

    User findByUsernameAndRoleAndState(String userName, UserRole role, UserState state);

    List<User> findByRole(UserRole role);
}
