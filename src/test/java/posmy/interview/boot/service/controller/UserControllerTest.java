package posmy.interview.boot.service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.model.AccStatus;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.service.UserService;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
    
	@MockBean
	private UserService userService;

    private User user;
    private UserDto userDto;
    private UserDto newUserDto;
    
    @BeforeEach
    void init() {
    	user = User.builder().userid(12345L).username("testid").password("testpw").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
    	newUserDto = UserDto.builder().userid(12345L).username("testid").password("testpw").role(Role.MEMBER.toString()).accStatus(AccStatus.ACTIVE.toString()).build();
		userDto = UserDto.builder().userid(12345L).username("testid").password("testpw").role(Role.MEMBER.toString()).accStatus(AccStatus.ACTIVE.toString()).build();
    }

	@Test
	@WithMockUser(username = "test", roles={"LIBRARIAN"})
	public void testGetAllUser() throws Exception
	{
		when(userService.getAllUser()).thenReturn(List.of(user));
		this.mockMvc.perform(get("/lib/user").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());//.andExpect(jsonPath("$.userid").value(12345));
	}

	@Test
	@WithMockUser(username = "test", roles={"LIBRARIAN"})
	public void testCreateUser()  throws Exception
	{
		UserDto userdtolocal= UserDto.builder().username("test2").password("pass").accStatus("ACTIVE").role("MEMBER").build();
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(userdtolocal);
		
		when(userService.addUser(any(User.class))).thenReturn(user);
		mockMvc.perform(post("/lib/user")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(jsonStr)						
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isCreated())			   
			   .andExpect(header().string("Location", "http://localhost/lib/user/12345"));	
		
	}
	
	@Test
	@WithMockUser(username = "test", roles={"LIBRARIAN"})
	public void testUpdateUser()  throws Exception
	{
		UserDto userdtolocal= UserDto.builder().username("test2").password("pass").accStatus("ACTIVE").role("MEMBER").build();
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(userdtolocal);
		when(userService.updateUser(any(User.class))).thenReturn(user);
		mockMvc.perform(put("/lib/user")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(jsonStr)						
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk());		   	
	}
	
	@Test
	@WithMockUser(username = "test", roles={"LIBRARIAN"})
	public void libTestDeleteUser()  throws Exception
	{

	    when(userService.deleteUser(12345L,"test")).thenReturn(user);
	    mockMvc.perform(delete("/lib/user/{userid}", user.getUserid())
		   		.contentType(MediaType.APPLICATION_JSON)					
		   		.accept(MediaType.APPLICATION_JSON))
	    		.andExpect(status().isOk());	
	    verify(userService, times(1)).deleteUser(user.getUserid(),"test");
	}
	
	@Test
	@WithMockUser(username = "testid", roles={"MEMBER"})
	public void memberTestDeleteUser()  throws Exception
	{
	    when(userService.deleteUser(anyString())).thenReturn(user);
	    mockMvc.perform(delete("/mem/user/del", user.getUserid())
		   		.contentType(MediaType.APPLICATION_JSON)					
		   		.accept(MediaType.APPLICATION_JSON))
	    		.andExpect(status().isOk());	

	    verify(userService, times(1)).deleteUser(user.getUsername());
	}
}
