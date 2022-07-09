package posmy.interview.boot.respository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.model.UserDetail;


@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long>{
	
	Optional<UserDetail> findByUsername(String username);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM UserDetail u where (u.username is null or u.username = :username) and u.roles = :roles ")
	void deleteMemberByUsername(String username, String roles);
	
	
}
