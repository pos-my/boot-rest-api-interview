package posmy.interview.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.model.Member;
import posmy.interview.boot.repository.MemberRepository;

/**
 * @author Hafiz
 * @version 0.01
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
	private MemberRepository memberRepo;
	
	@Override
	public List<Member> findAllMembers() {
		return memberRepo.findAll();
	}

	@Override
	public Member findByMemberId(String memberId) {
		return memberRepo.findByMemberId(memberId);
	}

	@Override
	public Member saveMember(Member member) throws Exception {
		return memberRepo.save(member);
	}

	@Override
	public Member updateMember(Member member) throws Exception {
		return saveMember(member);
	}

	@Override
	public Member deleteByMemberId(String memberId) throws Exception {
		return memberRepo.deleteByMemberId(memberId);
	}

	@Override
	public void deleteMember(Member member) throws Exception {
		memberRepo.delete(member);
	}
}
