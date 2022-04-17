package posmy.interview.boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.entity.Member;

@Repository
public interface MemberRepository  extends JpaRepository<Member, Long> {
	 Optional<Member> findByFullName (String name);

}
