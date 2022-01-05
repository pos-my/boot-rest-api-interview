package posmy.interview.boot.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.model.database.BookEntity;
import posmy.interview.boot.model.database.UserEntity;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT t FROM UserEntity t WHERE " +
            "t.userName like COALESCE(:userName, t.userName) and " +
            "t.fullName like COALESCE(:fullName, t.fullName) and  " +
            "t.userId = COALESCE(:userId, t.userId)")
    Page<UserEntity> getUserEntitiesByUserNameAndFullNameAndUserId(
            @Param("userName") String userName,
            @Param("fullName") String fullName,
            @Param("userId") Integer userId,
            Pageable pageable);

    UserEntity findUserEntityByUserName(String username);

    UserEntity findUserEntityByUserId(Integer userId);

}
