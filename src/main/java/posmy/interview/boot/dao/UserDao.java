package posmy.interview.boot.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.entity.User;

@Repository
public interface UserDao extends CrudRepository<User, Long>{

	User findByUsername(String username);	
	Boolean existsByUsername(String username);
}
