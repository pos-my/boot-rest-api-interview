package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.RolesEntity;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
    RolesEntity findByName(String name);
}
