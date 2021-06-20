package posmy.interview.boot.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

}
