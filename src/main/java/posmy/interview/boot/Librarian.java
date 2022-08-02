package posmy.interview.boot;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Librarian {
	
	private static final Logger log = LoggerFactory.getLogger(Librarian.class);
	
	 private BookShelf shelf;
	 private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

	 Librarian(BookShelf bookShelf, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
	    this.shelf = bookShelf;
	    this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
	  }


	// add, update, and remove Books from the system 
	 
	@GetMapping("/librarian/getBooks")
	public List<Book> all() {
		return shelf.findAll();
	}
	
	@GetMapping("/librarian/getBook/{name}")
	public List<Book> findByName(@PathVariable String name) {
		return shelf.findByName(name);
	}

	@PostMapping("/librarian/addBook/{name}")
	public String addBook(@PathVariable String name) {
		String ret = "Failed to add book.";
				
		if(isLibrarian()) {			
			Book book = new Book(name);
			shelf.save(book);
			ret = "New book added: " + name;
			return ret;			
		}else {			
			ret = "Only Librarian is allowed to add book.";
			return ret;
		}		
	}
	

	@PostMapping("/librarian/addBook")
	public String addBook(@RequestBody Book book) {
		String ret = "Failed to add book.";				
		if(isLibrarian()) {					
			shelf.save(book);
			ret = "New book added: " + book.getName();
			return ret;			
		}else {			
			ret = "Only Librarian is allowed to add book.";
			return ret;
		}		
	}
	
	@PostMapping("/librarian/remBook/name={name}")
	public String removeBook(@PathVariable String name) {
		String ret = "Book Not Found.";
		if (isLibrarian()) {
			List<Book> bookList = shelf.findByName(name);
			for(int i = 0; i<bookList.size(); i++){
				shelf.delete(bookList.get(i));
				ret = name + " is removed from shelf.";
			}
		}else {
			ret = "Only Librarian is allowed to remove book.";
		}
		
		return ret;
	}
	
	@PostMapping("/librarian/remBook/id={idStr}")
	public String removeBookByID(@PathVariable String idStr) {
		long id = Long.parseLong(idStr);  
		String ret = "Book Not Found.";
		if (isLibrarian()) {
			List<Book> bookList = shelf.findById(id);
			if(bookList != null && bookList.size() > 0) {
				shelf.delete(bookList.get(0));
				ret = id + "," + bookList.get(0).getName() + " is removed from shelf.";
			}
		}else {
			ret = "Only Librarian is allowed to remove book.";
		}
		
		return ret;
	}
	
	
	@PostMapping("/librarian/updBook")
	public String updateBook(@RequestBody Book book) {
		String ret = "Book Not Found.";
		if (isLibrarian()) {			
			shelf.save(book);
			ret = book.getName() + " updated.";			
		}else {
			ret = "Only Librarian is allowed to update book.";
		}		
		return ret;		 
	}
	
	
	@PostMapping("/librarian/addMember/{username}/{password}")
	public String addMember(@PathVariable String username, @PathVariable String password) {
		String ret = "Failed to add new member.";
		if(isLibrarian()) {
			if (inMemoryUserDetailsManager.userExists(username)) {
				ret = "Cannot add existing member:" + username;
			}else {
				inMemoryUserDetailsManager.createUser(User.withUsername(username).password("{noop}"+password).roles("MEMBER").build());
				if (inMemoryUserDetailsManager.userExists(username)) {
					log.info("user exists:"+username+"|"+inMemoryUserDetailsManager.userExists(username));
					ret = "New member added : " + username;	
				}
			}
		}else {			
			ret = "Only Librarian is allowed to add new member.";			
		}		
		return ret;
	}
	
	@PostMapping("/librarian/remMember/{username}")
	public String removeMember(@PathVariable String username) {
		String ret = "Failed to remove member.";
		if(isLibrarian()) {			
			if (inMemoryUserDetailsManager.userExists(username)) {
				inMemoryUserDetailsManager.deleteUser(username);				
				ret = "Removed member:" + username;
			}
		}else {			
			ret = "Only Librarian is allowed to remove member.";			
		}		
		return ret;
	}
	
	@PostMapping("/librarian/updMember/{username}/{password}")
	public String updateMember(@PathVariable String username, @PathVariable String password ) {
		String ret = "Failed to update member.";		 
		if(isLibrarian()) {			
			if (inMemoryUserDetailsManager.userExists(username)) {
				inMemoryUserDetailsManager.updateUser(User.withUsername(username).password("{noop}"+password).roles("MEMBER").build());				
				ret = "Updated member:" + username;
			}else {
				ret = "Member not found:" + username;
			}
		}else {			
			ret = "Only Librarian is allowed to update member.";			
		}		
		return ret;
	}
	
	@GetMapping("/librarian/getMembers")
	public String allMembers() {
		// not implemented
		return "List of members.";
	}
	
	
	
	
	

	

	
	
	
	private boolean isLibrarian() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = auth.getAuthorities().stream()
		          .anyMatch(r -> r.getAuthority().equals("ROLE_LIBRARIAN"));
		return hasUserRole;
	}

}
