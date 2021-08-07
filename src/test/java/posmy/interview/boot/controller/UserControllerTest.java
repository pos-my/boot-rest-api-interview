package posmy.interview.boot.controller;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import posmy.interview.boot.entity.Role;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.object.BookObject;
import posmy.interview.boot.object.UserObject;
import posmy.interview.boot.repository.IUserRepository;
import posmy.interview.boot.security.TokenAuthenticationFilter;
import posmy.interview.boot.security.TokenAuthenticationProvider;
import posmy.interview.boot.security.UserAuthenticationService;
import posmy.interview.boot.service.IUserService;
import posmy.interview.boot.service.impl.UserServiceImpl;

@WebMvcTest(UserController.class)
public class UserControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	IUserRepository userRepo;
	
	@MockBean 
	TokenAuthenticationProvider tokenAuthenticationProvider;
	
	@MockBean
	TokenAuthenticationFilter tokenAuthenticationFilter;
	
	@MockBean
	IUserService userService;
	
//	@MockBean
//	UserServiceImpl userServiceImpl;
	
	@MockBean
	UserAuthenticationService authentication;
	
	@Test
	public void create() throws Exception{
		
	}
	Role ROLE_1 = new Role(1L, "LIBRARIAN");
	Role ROLE_2 = new Role(2L, "MEMBER");
	
	User USER_1 = new User(1l, "uuid1","usertest1","user1test","Sirius Black", ROLE_1, false);
	User USER_2 = new User(2l, "uuid2","usertest2","user2test","Johny Depp", ROLE_2, false);

    @Autowired
    ObjectMapper mapper;
	
	@Test
	public void getUserList() throws Exception {
		
		List<User> users = new ArrayList<User>();
		users.add(USER_1);
		users.add(USER_2);
		
		//Mockito.when(userRepo.findAll()).thenReturn(users);
		//Mockito.when(userRepo.findByUuid("uuid1")).thenReturn(USER_1);
		Mockito.when(userService.findAll()).thenReturn(Optional.of(users));
				
		mockMvc.perform(MockMvcRequestBuilders
				.get("/user/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();	
	}
	

	@Test
	public void createUser() throws Exception{
		UserObject userObj_1 = new UserObject("usertest3", "user3test", "Tom Riddle", 1L);
		Mockito.when(authentication.findByToken("uuid1")).thenReturn(Optional.of(USER_1));
		Mockito.when(userService.update(userObj_1, 1L)).thenReturn(Optional.of(USER_1));
		
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/user/create")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(userObj_1)))
				.andExpect(status().isOk())
				.andReturn();		
	}
	
	@Test
	public void updateUser() throws Exception{

		UserObject userObj_1 = new UserObject("usertest1", "user1test", "Sirius White", 1L);
		Mockito.when(authentication.findByToken("uuid1")).thenReturn(Optional.of(USER_1));
		Mockito.when(userService.update(userObj_1, 1L)).thenReturn(Optional.of(USER_1));
		
		mockMvc.perform(MockMvcRequestBuilders
				.put("/user/update/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(userObj_1)))
				.andExpect(status().isOk())
				.andReturn();		
	}
	
	@Test
	public void getUser() throws Exception{
		Mockito.when(authentication.findByToken("uuid1")).thenReturn(Optional.of(USER_1));
		Mockito.when(userService.findByUserId(1L)).thenReturn(Optional.of(USER_1));
		mockMvc.perform(MockMvcRequestBuilders
				.put("/user/get/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();	
	}
	
	@Test
	public void deleteUser() throws Exception{
		Mockito.when(userService.delete(1L)).thenReturn(Optional.of(true));
		mockMvc.perform(MockMvcRequestBuilders
				.put("/user/delete/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();	
	}
	
}
