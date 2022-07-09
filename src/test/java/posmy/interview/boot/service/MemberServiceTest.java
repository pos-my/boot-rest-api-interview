package posmy.interview.boot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import posmy.interview.boot.constant.Roles;
import posmy.interview.boot.model.UserDetail;
import posmy.interview.boot.model.rest.GetMemberRequest;
import posmy.interview.boot.model.rest.GetMemberResponse;
import posmy.interview.boot.model.rest.MemberRequest;
import posmy.interview.boot.model.rest.MemberResponse;
import posmy.interview.boot.respository.UserDetailRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	
	@Mock
	private UserDetailRepository userDetailRepository;
	
	private MemberService underTest;
	
	@BeforeEach
	void setUp() {
		
		underTest = new MemberServiceImpl(userDetailRepository);
	}
	
	@Test
	void checkIfGetAllMemberSuccess() {
		//given
		//Initialize and assign entity
		UserDetail userDetail = new UserDetail();
		userDetail.setUsername("Ali");
		userDetail.setAge(23);
		userDetail.setPassword("password");
		userDetail.setRoles(Roles.MEMBER.getGrantedAuthoritiesRole());
		
		UserDetail userDetail1 = new UserDetail();
		userDetail1.setUsername("Kajing");
		userDetail1.setAge(23);
		userDetail1.setPassword("password");
		userDetail1.setRoles(Roles.MEMBER.getGrantedAuthoritiesRole());
		
		List<UserDetail> userDetailList = new ArrayList<>();
		userDetailList.add(userDetail);
		userDetailList.add(userDetail1);
		
		when(userDetailRepository.findAll()).thenReturn(userDetailList);
		
		//when
		GetMemberResponse finalResponse = underTest.getAllMember();
		
		//then
		assertThat(finalResponse).isNotNull();
	}
	
	@Test
	void checkIfGetMemberByUsernameSuccess() throws Exception {
		//given
		//Initialize and assign rest request
		MemberRequest request = new MemberRequest();
		request.setUsername("Ali");
		
		//Initialize and assign entity
		UserDetail userDetail = new UserDetail();
		userDetail.setUsername("Ali");
		userDetail.setAge(23);
		userDetail.setPassword("password");
		userDetail.setRoles(Roles.MEMBER.getGrantedAuthoritiesRole());
		
		Optional<UserDetail> optional = Optional.of(userDetail);
		
		when(userDetailRepository.findByUsername(request.getUsername())).thenReturn(optional);
		
		//when 
		MemberResponse response  = underTest.getMemberByUsername(request);
		
		//then 
		assertThat(response).isNotNull();
		
	}
	
	@Test
	void checkIfAddMemberSuccess(){
		//given
		//Initialize and assign rest request
		MemberRequest request = new MemberRequest();
		request.setUsername("Ali");
		request.setAge(23);
		request.setPassword("password");
		
		//Initialize and assign entity
		UserDetail userDetail = new UserDetail();
		userDetail.setUsername(request.getUsername());
		userDetail.setPassword(request.getPassword());
		
		when(userDetailRepository.save(any(UserDetail.class))).thenReturn(userDetail);
		
		//when
		MemberResponse response = underTest.addMember(request);
		
		//then 
		assertThat(response).isNotNull();
		
	}
	
	@Test
	void checkIfUpdateMemberSuccess(){
		//given
		//Initialize and assign rest request
		MemberRequest request = new MemberRequest();
		request.setUsername("Jessi");
		request.setAge(23);
		request.setPassword("password");
		
		//Initialize and assign entity
		UserDetail userAfter = new UserDetail();
		userAfter.setUsername(request.getUsername());
		userAfter.setAge(request.getAge());
		userAfter.setPassword("newPassword");
		
		when(userDetailRepository.save(any(UserDetail.class))).thenReturn(userAfter);
		
		//when
		MemberResponse response = underTest.addMember(request);
		
		//then 
		assertThat(response.getMemberDetail().getUsername()).isEqualTo(request.getUsername());
		
	}
	
	@Test
	void checkIfDeleteMemberSuccess() throws Exception {
		
		//given
		//Initialize Rest Request
		GetMemberRequest request = new GetMemberRequest();
		request.setUsername("Jessi");
		
		//Initialize and assign entity
		UserDetail userDetail = new UserDetail();
		userDetail.setUsername(request.getUsername());
		userDetail.setPassword("password");
		userDetail.setAge(20);
		userDetail.setRoles(Roles.MEMBER.name());
		
		Optional<UserDetail> optional = Optional.of(userDetail);
		
		when(userDetailRepository.findByUsername(request.getUsername())).thenReturn(optional);
		
		//when
		underTest.deleteMember(request);
		
		//then
		verify(userDetailRepository, times(1)).deleteMemberByUsername(request.getUsername(), Roles.MEMBER.getGrantedAuthoritiesRole());
		
		
		
		
		
	}

}
