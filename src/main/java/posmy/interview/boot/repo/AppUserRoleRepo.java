package posmy.interview.boot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.AppUserRole;

import java.util.Optional;

public interface AppUserRoleRepo extends JpaRepository<AppUserRole, Long> {
    Optional<AppUserRole> findByName(String name);
}
