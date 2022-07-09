package posmy.interview.boot.service;


import posmy.interview.boot.model.rest.GetMemberRequest;
import posmy.interview.boot.model.rest.GetMemberResponse;
import posmy.interview.boot.model.rest.MemberDetail;
import posmy.interview.boot.model.rest.MemberRequest;
import posmy.interview.boot.model.rest.MemberResponse;

public interface MemberService {

	public GetMemberResponse getAllMember();
	
	public MemberResponse getMemberByUsername(MemberRequest request) throws Exception;
	
	public MemberResponse addMember(MemberRequest memberRequest);
	
	public MemberResponse updateMember(MemberRequest memberRequest);
	
	public String deleteMember(GetMemberRequest memberRequest) throws Exception;
	
	 
}
