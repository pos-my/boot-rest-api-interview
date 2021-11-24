package posmy.interview.boot.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import posmy.interview.boot.domain.User;
import posmy.interview.boot.enums.UserRole;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, PagingAndSortingRepository<User, UUID> {

    Page<User> findAllByRole(UserRole userRole, Pageable pageable);

    Optional<User> findUserByUsername(String username);
}
