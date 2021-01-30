package posmy.interview.boot.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.MemberEntity;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.CreateMemberRequest;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.model.UpdateMemberRequest;
import posmy.interview.boot.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public MemberEntity createMember(CreateMemberRequest createMemberRequest) {
        MemberEntity memberEntity = modelMapper.map(createMemberRequest, MemberEntity.class);
        return memberRepository.save(memberEntity);
    }

    @Override
    public void deleteMember(long memberId) throws NoDataFoundException {
        MemberEntity bookEntity = memberRepository.findById(memberId).orElse(null);
        if(bookEntity == null) {
            throw new NoDataFoundException("No member found for delete");
        }
        memberRepository.deleteById(memberId);
    }

    @Override
    public MemberEntity updateMember(UpdateMemberRequest updateMemberRequest) throws NoDataFoundException {
        MemberEntity memberEntity = memberRepository.findById(updateMemberRequest.getMemberId()).orElseThrow(() -> new NoDataFoundException("Member not found"));
        memberEntity.setFirstName(updateMemberRequest.getFirstName());
        memberEntity.setLastName(updateMemberRequest.getLastName());
        memberEntity.setEmail(updateMemberRequest.getEmail());
        return memberRepository.save(memberEntity);
    }

    @Override
    public Member getMember(long memberId) throws NoDataFoundException {
        log.info("Get member by id {}", memberId);
        log.info("Name of Member: {}", SecurityContextHolder.getContext().getAuthentication().getName());
        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new NoDataFoundException("Member not found"));
        return modelMapper.map(memberEntity, Member.class);
    }
}
