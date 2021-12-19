package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>, JpaSpecificationExecutor<User>   {

	@Query(value= "SELECT o FROM User o WHERE o.username = :username")
	User findByUsername(@Param("username") String username);

	@Query(value= "SELECT o FROM User o WHERE o.username = :username")
	User findRoleUser(@Param("username") String username);

	@Query(value= "SELECT o FROM User o WHERE o.roleId = :roleId")
	User findUserByRole(@Param("roleId") int roleId);

	@Modifying
	@Query(value= "UPDATE User o SET o.username = :username, o.roleId = :roleId, o.isDeleted = :deleted WHERE o.id = :userId")
	User updateMember(@Param("userId") int id, @Param("username") String username, @Param("roleId") int roleId, @Param("deleted") boolean deleted);

	@Modifying
	@Query(value= "UPDATE User o SET o.isDeleted = :deleted WHERE o.id = :userId")
	void deleteAcc(@Param("userId") int userId, @Param("deleted") boolean isDeleted);
	
}
