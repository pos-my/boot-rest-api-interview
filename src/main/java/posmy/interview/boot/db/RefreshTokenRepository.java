package posmy.interview.boot.db;

import org.springframework.data.repository.CrudRepository;
import posmy.interview.boot.model.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken findByToken(String token);
}
