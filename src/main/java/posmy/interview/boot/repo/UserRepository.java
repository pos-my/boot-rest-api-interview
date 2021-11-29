package posmy.interview.boot.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndStatus(String username, String status);

    Page<User> findAllByRole(String role, Pageable pageable);
}
