package posmy.interview.boot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import posmy.interview.boot.model.UserDetail;
import posmy.interview.boot.respository.UserDetailRepository;


@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserDetailRepository underTest;
	
	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}
	
	@Test
	void findIfBookExistByUsingByBookId() {
		
		String username = "test";
		UserDetail user = new UserDetail();
		user.setUsername(username);
		user.setRoles("ROLE_LIBRARIAN");
		user.setPassword("password");
		user.setAge(23);
		
		underTest.save(user);
		
		//when
		UserDetail finalUser = underTest.findByUsername(username).get();
		
		assertThat(finalUser).isNotNull();
	}
	
	
	@Test
	void checkIfMemberDeletedByUsingUsernameAndRoles() {
		
		UserDetail user = new UserDetail();
		user.setUsername("test");
		user.setRoles("ROLE_LIBRARIAN");
		user.setPassword("password");
		user.setAge(23);
		
		//Added one userDetail 
		underTest.save(user);
		
		long countBeforeDelete = underTest.count();
		
		long countAfterDeleteOneMember = countBeforeDelete-1;
		
		long finalCount = 0;
		
		if(countAfterDeleteOneMember >= 0) {
			
			finalCount =countAfterDeleteOneMember;
		}
		
		//Delete the only user
		underTest.deleteMemberByUsername(user.getUsername(), user.getRoles());
		
		//Check count
		assertThat(underTest.count()).isEqualTo(finalCount);
		
	}

	
}
