package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	 @Query("SELECT u FROM User u WHERE u.username = :username")
	 public User findByUsername(@Param("username") String username);
}
