package posmy.interview.boot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.AppUser;

import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
