package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import posmy.interview.boot.constant.Roles;
import posmy.interview.boot.model.UserDetail;
import posmy.interview.boot.model.rest.MemberRequest;
import posmy.interview.boot.respository.UserDetailRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SystemUserServiceTest {

	@Mock
	private UserDetailRepository userDetailRepository;
	
	private SystemUserService underTest;
	
	@BeforeEach
	void setUp() {
		
		underTest = new SystemUserService(userDetailRepository);
	}
	
	@Test
	void loadUserByUsername() {
		
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
		
		
		
		when(userDetailRepository.findByUsername(any(String.class))).thenReturn(optional);
		
		//when
		UserDetails userDetails = underTest.loadUserByUsername(request.getUsername());
		
		//then
		assertThat(userDetails.getUsername()).isEqualTo(userDetail.getUsername());
		
	}
}
