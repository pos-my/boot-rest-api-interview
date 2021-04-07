package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findFirstByLoginId(String loginId);

    @Query("FROM User AS u INNER JOIN u.roles AS r WHERE r.name = ?1")
    List<User> findByRoleName(String roleName);
}
