package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.model.User;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * find user by user name
     *
     * @param username user name
     * @return user
     */
    Optional<User> findByUsername(String username);
}
