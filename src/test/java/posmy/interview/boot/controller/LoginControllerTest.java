package posmy.interview.boot.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import posmy.interview.boot.entity.Role;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.security.TokenAuthenticationProvider;
import posmy.interview.boot.security.UserAuthenticationService;
import posmy.interview.boot.service.IUserService;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	UserAuthenticationService authentication;
	
	@MockBean
	IUserService userService;
	
	@MockBean 
	TokenAuthenticationProvider tokenAuthenticationProvider;
	
	Role ROLE_1 = new Role(1L, "LIBRARIAN");
	Role ROLE_2 = new Role(2L, "MEMBER");
	
	User USER_1 = new User(1l, "uuid1","usertest1","user1test","Sirius Black", ROLE_1, false);
	User USER_2 = new User(2l, "uuid2","usertest2","user2test","Johny Depp", ROLE_2, false);
	
	@Test
	public void login_sucess() throws Exception{
		
		Mockito.when(authentication.login("usertest1", "user1test")).thenReturn(Optional.of("uuid1"));
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/login?username=usertest1&password=user1test")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("uuid1"));
	}
}
