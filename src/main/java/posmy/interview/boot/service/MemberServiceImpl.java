package posmy.interview.boot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import posmy.interview.boot.constant.Roles;
import posmy.interview.boot.model.UserDetail;
import posmy.interview.boot.model.rest.GetMemberRequest;
import posmy.interview.boot.model.rest.GetMemberResponse;
import posmy.interview.boot.model.rest.MemberDetail;
import posmy.interview.boot.model.rest.MemberRequest;
import posmy.interview.boot.model.rest.MemberResponse;
import posmy.interview.boot.respository.UserDetailRepository;

@Service
public class MemberServiceImpl implements MemberService{
	
	private UserDetailRepository userDetailRepository;
	
	public MemberServiceImpl(UserDetailRepository userDetailRepository) {
		this.userDetailRepository = userDetailRepository;
	}
	
	@Override
	public GetMemberResponse getAllMember() {
		//Initialize rest response
		GetMemberResponse response = new GetMemberResponse();
		List<MemberDetail> memberDetails = new ArrayList<MemberDetail>();
		
		//Get All user 
		List<UserDetail> userList = userDetailRepository.findAll();
		
		//Get only member role user
		for(UserDetail member: userList) {
			
			if(member.getRoles() != null) {
			
				if(member.getRoles().equals(Roles.MEMBER.getGrantedAuthoritiesRole())) {
					
					MemberDetail memberDetail = new MemberDetail();
					
					memberDetail.setUsername(member.getUsername());
					memberDetail.setAge(member.getAge());
					memberDetail.setFirstName(member.getFirstName());
					memberDetail.setLastName(member.getLastName());
					
					memberDetails.add(memberDetail);
					
				}
			
			}
			
		}
		
		response.setMember(memberDetails);
		
		return response;
		
		
	}

	@Override
	public MemberResponse getMemberByUsername(MemberRequest request) throws Exception {
		//Initialize rest response
		MemberResponse response = new MemberResponse();
		MemberDetail memberDetail = new MemberDetail();
		
		//Get member by username
		UserDetail member = userDetailRepository.findByUsername(request.getUsername()).get();
		
		//throw exception if member does not exist
		if(member == null) {
			
			throw new Exception("Username not found");
		}
		
		memberDetail.setUsername(member.getUsername());
		memberDetail.setAge(member.getAge());
		memberDetail.setFirstName(member.getFirstName());
		memberDetail.setLastName(member.getLastName());
	
		response.setMemberDetail(memberDetail);
		
		return response;
		
	}

	@Override
	public MemberResponse addMember(MemberRequest memberRequest){
		//Initialize rest response
		MemberResponse memberResponse = new MemberResponse();
		UserDetail member = new UserDetail();
		
		member.setUsername(memberRequest.getUsername());
		member.setAge(memberRequest.getAge());
		member.setPassword(memberRequest.getPassword());
		
		member.setRoles(Roles.MEMBER.getGrantedAuthoritiesRole());
		
		//Add member to db
		UserDetail response = userDetailRepository.save(member);
		
		MemberDetail memberDetail = new MemberDetail();
		memberDetail.setUsername(response.getUsername());
		memberDetail.setAge(response.getAge());
		memberDetail.setFirstName(response.getFirstName());
		memberDetail.setLastName(response.getLastName());
		
		memberResponse.setMemberDetail(memberDetail);
		
		return memberResponse;
	}

	@Override
	public MemberResponse updateMember(MemberRequest memberRequest){
		//Initialize rest response
		MemberResponse memberResponse = new MemberResponse();
		UserDetail member = new UserDetail();
		
		member.setUsername(memberRequest.getUsername());
		member.setPassword(memberRequest.getPassword());
		member.setAge(memberRequest.getAge());
		member.setRoles(Roles.MEMBER.getGrantedAuthoritiesRole());
		member.setFirstName(memberRequest.getFirstName());
		member.setLastName(memberRequest.getLastName());
		
		//Update the member detail
		UserDetail response = userDetailRepository.save(member);
		
		MemberDetail memberDetail = new MemberDetail();
		memberDetail.setUsername(response.getUsername());
		memberDetail.setAge(response.getAge());
		memberDetail.setFirstName(response.getFirstName());
		memberDetail.setLastName(response.getLastName());
		
		memberResponse.setMemberDetail(memberDetail);
		
		return memberResponse;
	}

	@Override
	public String deleteMember(GetMemberRequest memberRequest) throws Exception{
		//Initialize entity
		UserDetail member = new UserDetail();
		
		//Check if username exist
		member = userDetailRepository.findByUsername(memberRequest.getUsername()).get();
		
		userDetailRepository.deleteMemberByUsername(member.getUsername(), Roles.MEMBER.getGrantedAuthoritiesRole());
		
		return "Delete Successfully";
	}



	


}
