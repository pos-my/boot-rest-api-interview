package posmy.interview.boot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
