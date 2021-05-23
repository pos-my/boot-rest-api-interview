package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.Authorities;

@Repository
public interface AuthorityRepository extends JpaRepository<Authorities, Long> {
}
