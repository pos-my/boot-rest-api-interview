package posmy.interview.boot.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void givenUsername_whenFindByUsername_thenAbleToGetUser() {
		User result = userRepository.findByUsername("alex");
		
		assertNotNull(result);
		assertEquals("alex", result.getUsername());
	}

}
