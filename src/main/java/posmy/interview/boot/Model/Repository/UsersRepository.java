package posmy.interview.boot.Model.Repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import posmy.interview.boot.Model.Book;
import posmy.interview.boot.Model.Users;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    @Query("SELECT u FROM Users u WHERE u.role = 'USER'")
    List<Users> findAllUserWithUserRole();

}
