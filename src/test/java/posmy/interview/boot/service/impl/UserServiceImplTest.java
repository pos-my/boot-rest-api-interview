package posmy.interview.boot.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import posmy.interview.boot.exception.CustomException;
import posmy.interview.boot.exception.ResourceAlreadyExists;
import posmy.interview.boot.exception.ResourceNotFoundException;
import posmy.interview.boot.model.AccStatus;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.service.UserService;


@SpringBootTest
public class UserServiceImplTest  {
	
	@Autowired
	private UserService userService;
	
	@MockBean
    private UserRepository userRepository;
	
	
    @Test
    public void addUserTest() {
    	User user1 = User.builder().username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
    	when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		when(userRepository.save(any(User.class))).thenReturn(user1);
    	User user = userService.addUser(user1);
    	assertEquals(user.getUsername(),user1.getUsername());
    }

    @Test
    public void addUserTest_ResourceExist() {
    	User user1 = User.builder().username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
    	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));
    	assertThrows(ResourceAlreadyExists.class, () -> {
    		User user = userService.addUser(user1);
    	});
    }
    
 
    @Test
    public void updateUserTest() {
	    User user1 = User.builder().username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
	    User user2 =User.builder().username("testnew").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
	    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));
	    when(userRepository.save(any(User.class))).thenReturn(user2);
		User userCreated = userService.updateUser(user1);
		assertNotEquals(userCreated.getUsername(),user1.getUsername());
    }
    
    @Test
    public void updateUserTest_ResourceNotFound() {
	    User user1 = User.builder().username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
	    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    	assertThrows(ResourceNotFoundException.class, () -> {
    		User userCreated = userService.updateUser(user1);
    	 });
    }

    @Test
    public void getAllUserTest() {
	    User user1 = User.builder().userid(1L).username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
	    User user2 = User.builder().userid(2L).username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
	    User user3 = User.builder().userid(3L).username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
	    List<User> userList = new ArrayList<User>();
	    userList.add(user1);
	    userList.add(user2);
	    userList.add(user3);
        
	    when(userRepository.findAll()).thenReturn(userList);
		List<User> users = userService.getAllUser();
		assertEquals(users.size(),userList.size());
    }
    
    @Test
    public void LibrarianDeleteUserTest() {
		User user1 = User.builder().userid(1L).username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
		User admin = User.builder().userid(2L).username("admin").password("password").role(Role.LIBRARIAN).accStatus(AccStatus.ACTIVE).build();
		when(userRepository.findById(user1.getUserid())).thenReturn(Optional.of(user1));
		when(userRepository.findByUsername(admin.getUsername())).thenReturn(Optional.of(admin));
		when(userRepository.save(any(User.class))).thenReturn(user1);
		User deletedUser = userService.deleteUser(user1.getUserid(),admin.getUsername());
		assertEquals(deletedUser.getAccStatus(),AccStatus.DELETE);
	    //verify(userRepository).deleteById(user1.getUserid());
    }
    
    @Test
    public void LibrarianDeleteUserTest_CustomException() {
		User user1 = User.builder().userid(1L).username("admin1").password("password").role(Role.LIBRARIAN).accStatus(AccStatus.ACTIVE).build();
		User admin = User.builder().userid(2L).username("admin").password("password").role(Role.LIBRARIAN).accStatus(AccStatus.ACTIVE).build();
		when(userRepository.findById(user1.getUserid())).thenReturn(Optional.of(user1));
		when(userRepository.findByUsername(admin.getUsername())).thenReturn(Optional.of(admin));
		
    	assertThrows(CustomException.class, () -> {
    		User deletedUser = userService.deleteUser(user1.getUserid(),admin.getUsername());
    	 });
    }
    
    @Test
    public void MemberDeleteUserTest() {
    	User user1 = User.builder().userid(1L).username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
	    when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
	    when(userRepository.save(any(User.class))).thenReturn(user1);
	    User deletedUser = userService.deleteUser(user1.getUsername());
	    assertEquals(deletedUser.getAccStatus(),AccStatus.DELETE);
	    //verify(userRepository).deleteById(user1.getUserid());
    }

    @Test
    public void MemberDeleteUserTest_CustomException() {
    	
    	User user1 = User.builder().userid(1L).username("test").password("password").role(Role.MEMBER)
    			.accStatus(AccStatus.ACTIVE).build();
    	Book book1 = Book.builder().bookId(1L).isbn("12345").title("testBook").available("BORROWED").user(user1).build();
    	user1.setBook(Set.of(book1));
	    when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
    	assertThrows(CustomException.class, () -> {
    		User deletedUser = userService.deleteUser(user1.getUsername());
    	 });
    }

    
}
