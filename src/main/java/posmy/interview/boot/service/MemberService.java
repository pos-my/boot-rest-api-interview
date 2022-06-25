package posmy.interview.boot.service;

import java.util.List;

import posmy.interview.boot.model.Member;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface MemberService {

    List<Member> findAllMembers();
    Member findByMemberId(String memberId);
	Member saveMember(Member member) throws Exception;
	Member updateMember(Member member) throws Exception;
	Member deleteByMemberId(String memberId) throws Exception;
	void deleteMember(Member member) throws Exception;
}
