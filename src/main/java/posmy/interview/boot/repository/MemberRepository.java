package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByUsername(String username);
}
