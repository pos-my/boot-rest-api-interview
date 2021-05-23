package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
}
