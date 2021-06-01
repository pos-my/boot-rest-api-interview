package posmy.interview.boot.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.entity.Role;
import posmy.interview.boot.enums.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    
	List<Role> findAll();
	
	Optional<Role> findByRole( UserRole username );
	
	@Transactional
	@Modifying
    @Query( value="delete from user_role where user_id = :user_id", nativeQuery = true)
    void deleteByUserId( @Param("user_id") long user_id );
	
	@Transactional
	@Modifying
    @Query( value="insert into user_role ( user_id, role_id ) values ( :user_id , :role_id )", nativeQuery = true)
    void addUserRole( @Param("user_id") String user_id, @Param("role_id") String role_id );
	
}
