package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    public List<Member> viewAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> get(Long id) {
        return memberRepository.findById(id);
    }

    public Member add(Member member) {
        return memberRepository.save(member);

    }

    public Member update(Member member) {
        return memberRepository.save(member);

    }

    public Optional<Member> delete(Long id) {
        Optional<Member> maybeMember = memberRepository.findById(id);
        if (maybeMember.isPresent()) {
            Member member = maybeMember.get();
            if (!member.isDeleted()) {
                member.setDeleted(true);
                Member updatedMember = memberRepository.save(member);
                return Optional.of(updatedMember);
            } else {
                return Optional.of(member);
            }

        } else {
            return maybeMember;
        }
    }

}
