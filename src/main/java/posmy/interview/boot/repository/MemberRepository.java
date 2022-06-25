package posmy.interview.boot.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import posmy.interview.boot.model.Member;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface MemberRepository extends JpaRepository<Member, UUID> {

	Member findByMemberId(String memberId);
	Member deleteByMemberId(String memberId) throws Exception;
}
