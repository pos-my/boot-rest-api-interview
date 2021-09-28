package posmy.interview.boot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import posmy.interview.boot.exceptions.CustomRestApiException;
import posmy.interview.boot.models.RestApiSuccessRepsonse;
import posmy.interview.boot.models.daos.Member;
import posmy.interview.boot.models.dtos.member.UserRole;
import posmy.interview.boot.repositories.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MemberServiceImpl implements MemberService {

    private Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    private MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member getMemberById(Long memberId) throws CustomRestApiException {
        Optional<Member> memberData = memberRepository.findById(memberId);
        if (memberData.isEmpty()) {
            throw new CustomRestApiException("Member does not exist! MemberID: " + memberId, HttpStatus.NOT_FOUND);
        }
        return memberData.get();
    }

    @Override
    public List<Member> getAllMembers() throws CustomRestApiException {
        List<Member> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new CustomRestApiException("There are no members!", HttpStatus.NOT_FOUND);
        }
        return members;
    }

    @Override
    public RestApiSuccessRepsonse addMembers(List<Member> memberListRequest) throws CustomRestApiException {
        List<Member> membersToAdd = new ArrayList<>();
        for (Member member : memberListRequest) {
            Optional<Member> hasMember = memberRepository.findByEmail(member.getEmail());
            if (hasMember.isPresent()) {
                throw new CustomRestApiException("Member {" + member.getFirstName() + " " + member.getLastName()
                        + "} with email {" + member.getEmail() + "} already exists!", HttpStatus.CONFLICT);
            }
            membersToAdd.add(member);
        }
        for (Member memberToAdd : membersToAdd) {
            memberRepository.save(memberToAdd);
        }

        return new RestApiSuccessRepsonse(HttpStatus.OK, "Successfully added member(s).");
    }

    @Override
    public RestApiSuccessRepsonse updateMemberById(Long memberId, Member memberRequest) throws CustomRestApiException {
        Optional<Member> memberData = memberRepository.findById(memberId);
        if (memberData.isEmpty()) {
            throw new CustomRestApiException("Member does not exist! MemberID: " + memberId, HttpStatus.NOT_FOUND);
        }
        Member updateMember = memberData.get();
        updateMember.setFirstName(memberRequest.getFirstName());
        updateMember.setLastName(memberRequest.getLastName());
        updateMember.setPhone(memberRequest.getPhone());
        updateMember.setEmail(memberRequest.getEmail());
        updateMember.setUserRole(memberRequest.getUserRole());
        memberRepository.save(updateMember);

        return new RestApiSuccessRepsonse(HttpStatus.OK, "Successfully updated member.");
    }

    @Override
    public RestApiSuccessRepsonse removeMemberById(Long memberId) throws CustomRestApiException {
        Optional<Member> memberData = memberRepository.findById(memberId);
        if (memberData.isEmpty()) {
            throw new CustomRestApiException("Member does not exist! MemberID: " + memberId, HttpStatus.NOT_FOUND);
        }
        memberRepository.deleteById(memberId);

        return new RestApiSuccessRepsonse(HttpStatus.OK, "Successfully deleted member.");
    }

    @Override
    public UserRole getMemberUserRole(String email) throws Exception {
        Optional<Member> memberData = memberRepository.findByEmail(email);
        if (memberData.isEmpty()) {
            throw new CustomRestApiException("You are not allowed to access this data.", HttpStatus.FORBIDDEN);
        }
        return memberData.get().getUserRole();
    }
}
