package posmy.interview.boot.db;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.entity.UserEntity;

public interface UserDal extends JpaRepository<UserEntity, String> {
    UserEntity findByUserName(String userName);
}
