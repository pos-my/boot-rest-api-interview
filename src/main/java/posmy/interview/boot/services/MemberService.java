package posmy.interview.boot.services;

import posmy.interview.boot.models.RestApiSuccessRepsonse;
import posmy.interview.boot.models.daos.Member;
import posmy.interview.boot.models.dtos.member.UserRole;

import java.util.List;

public interface MemberService {

    Member getMemberById(Long memberId) throws Exception;

    List<Member> getAllMembers() throws Exception;

    RestApiSuccessRepsonse addMembers(List<Member> request) throws Exception;

    RestApiSuccessRepsonse updateMemberById(Long memberId, Member request) throws Exception;

    RestApiSuccessRepsonse removeMemberById(Long memberId) throws Exception;

    UserRole getMemberUserRole(String email) throws Exception;
}
