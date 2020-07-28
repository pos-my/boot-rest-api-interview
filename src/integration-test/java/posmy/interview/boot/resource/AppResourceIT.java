package posmy.interview.boot.resource;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import posmy.interview.boot.Application;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AppResourceIT {

	@Autowired
    private WebApplicationContext context;
 
    private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}
	
	@Test
    @WithMockUser(username = "test-user", authorities = { "LIBRARIAN" })
    public void givenUserAsLibrarian_whenViewBooks_thenSuccess() throws Exception {
		mockMvc.perform(get("/view-books").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = { "MEMBER" })
    public void givenUserAsMember_whenViewBooks_thenSuccess() throws Exception {
		mockMvc.perform(get("/view-books").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "UNKNOWN_ROLE" })
    public void givenUserAsUnknownRole_whenViewBooks_thenForbidden() throws Exception {
		mockMvc.perform(get("/view-books").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "LIBRARIAN" })
    public void givenUserAsLibrarian_whenCreateAndUpdateBook_thenSuccess() throws Exception {
		mockMvc.perform(get("/create-book").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
		
		mockMvc.perform(get("/update-book/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = { "MEMBER" })
    public void givenUserAsMember_whenCreateAndUpdateBook_thenForbidden() throws Exception {
		mockMvc.perform(get("/create-book").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/update-book/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "LIBRARIAN" })
    public void givenUserAsLibrarian_whenViewUsers_thenSuccess() throws Exception {
		mockMvc.perform(get("/view-users").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test-user", authorities = { "MEMBER" })
    public void givenUserAsMember_whenViewUsers_thenSuccess() throws Exception {
		mockMvc.perform(get("/view-users").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "LIBRARIAN" })
    public void givenUserAsLibrarian_whenCreateAndUpdateUser_thenSuccess() throws Exception {
		mockMvc.perform(get("/create-user").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
		
		mockMvc.perform(get("/update-user/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "MEMBER" })
    public void givenUserAsMember_whenCreateUser_thenForbidden() throws Exception {
		mockMvc.perform(get("/create-user").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "MEMBER" })
    public void givenUserAsMember_whenUpdateUser_thenForbidden() throws Exception {
		mockMvc.perform(get("/update-user/{id}", 2L).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "MEMBER" })
    public void givenUserAsMember_whenBorrowAndReturnBook_thenRedirected() throws Exception {
		mockMvc.perform(post("/books/borrow/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is3xxRedirection());
		
		mockMvc.perform(post("/books/return/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "LIBRARIAN" })
    public void givenUserAsLibrarian_whenBorrowAndReturnBook_thenForbidden() throws Exception {
		mockMvc.perform(post("/books/borrow/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
		
		mockMvc.perform(post("/books/return/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "MEMBER" })
    public void givenUserAsMember_whenDeleteUserAndDeleteAllUsers_thenRedirectedAndForbidden() throws Exception {
		mockMvc.perform(post("/users/delete/3").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is3xxRedirection());
		
		mockMvc.perform(post("/users/delete-all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username = "test-user", authorities = { "LIBRARIAN" })
    public void givenUserAsLibrarian_whenDeleteUserAndDeleteAllUsers_thenRedirected() throws Exception {
		mockMvc.perform(post("/users/delete/4").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is3xxRedirection());
		
		mockMvc.perform(post("/users/delete-all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is3xxRedirection());
    }
}
