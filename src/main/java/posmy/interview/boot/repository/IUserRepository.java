package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import posmy.interview.boot.entity.User;

public interface IUserRepository extends JpaRepository<User,Long>{
	User findByUsername(String username);
	
	User findByUsernameAndPassword(String username, String password);
	
	User findByUuid(String uuid);
}
