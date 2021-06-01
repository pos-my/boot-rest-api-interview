package posmy.interview.boot.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import posmy.interview.boot.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
	List<User> findAll();
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findById(long id);
	
	@Transactional
	@Modifying
	@Query(value="delete from users where id = :id", nativeQuery = true)
	void deleteById( Long id );
	
	@Transactional
	@Modifying
    @Query( value="update users set username = :username, password = :password, firstname = :firstname, lastname = :lastname, status = :status where id = :id", nativeQuery = true)
    void updateUser(@Param("id") String id, @Param("username") String username, @Param("password") String password, @Param("firstname") String firstname, @Param("lastname") String lastname, @Param("status") String status );

	@Transactional
	@Modifying
    @Query( value="insert into user_role ( user_id, role_id ) values ( :user_id , :role_id )", nativeQuery = true)
    void addUserRole(@Param("user_id") long user_id, @Param("role_id") long role_id );
	
	@Transactional
	@Modifying
    @Query( value="delete from user_role where user_id = :user_id ", nativeQuery = true)
    void deleteAllUserRoleByUserId( @Param("user_id") long user_id );
	
	@Transactional
	@Modifying
    @Query( value="update users set status = :status where user_id = :user_id ", nativeQuery = true)
    void activateUserByUserId( @Param("user_id") long user_id, @Param("status") String status );
	
	

	
	
}
