package posmy.interview.boot.service.member;

import posmy.interview.boot.entity.MemberEntity;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.CreateMemberRequest;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.model.UpdateMemberRequest;

public interface MemberService {
    MemberEntity createMember(CreateMemberRequest createMemberRequest);
    void deleteMember(long memberId) throws NoDataFoundException;
    MemberEntity updateMember(UpdateMemberRequest updateMemberRequest) throws NoDataFoundException;
    Member getMember(long memberId) throws NoDataFoundException;
}
