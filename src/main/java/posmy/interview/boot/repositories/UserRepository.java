package posmy.interview.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.domains.Users;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users, Long> {
    long deleteByUserName(String userName);

    Optional<Users> findByUserName(String userName);
}
