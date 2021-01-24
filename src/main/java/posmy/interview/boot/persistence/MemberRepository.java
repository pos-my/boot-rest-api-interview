package posmy.interview.boot.persistence;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Qualifier("members")
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByName(String name);
}
