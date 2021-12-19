package posmy.interview.boot;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.service.SystemService;
import posmy.interview.boot.service.impl.SystemServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application.properties")
public class ApplicationIntegrationTest {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private UserRepository userRepository;
	
	@TestConfiguration
    class SystemServiceImplTestContextConfiguration {
        @Bean
        public SystemService systemService() {
            return new SystemServiceImpl();
    }
	}
	
	@Before(value = "whenGetUser_thenStatus200")
	public void setUp() {
	    User user = new User(1, "Jackson", false, 2);

	    Mockito.when(userRepository.findByUsername(user.getUsername()))
	      .thenReturn(user);
	}
	
	@Test
	public void whenGetUser_thenStatus200()
	  throws Exception {

		String username = "Jackson";
		User user = userRepository.findByUsername(username);
		assertThat(user.getUsername()).isEqualTo(username);
		
		mvc.perform(get("/system/check/user/status")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void whenAddMember_thenStatus200()
	  throws Exception {
		
		User user = new User(1, "Jackson", false, 2);
		Mockito.when(userRepository.save(user))
	      .thenReturn(null);

		String username = "Jackson";
		User user2 = userRepository.findByUsername(user.getUsername());
		assertThat(user2.getUsername()).isEqualTo(username);
		
		mvc.perform(post("/system/add/member")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void whenUpdateMember_thenStatus200()
	  throws Exception {
		
		User user = new User(1, "Jackson", false, 2);
		Mockito.when(userRepository.updateMember(user.getId(), user.getUsername(), user.getRoleId(), user.isDeleted()))
	      .thenReturn(null);

		String userId = "Jackson";
		User user2 = userRepository.findByUsername(user.getUsername());
		assertThat(user2.getId()).isEqualTo(userId);
		
		mvc.perform(put("/system/update/member")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
}
