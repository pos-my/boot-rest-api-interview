package posmy.interview.boot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Member {
	
	private static final Logger log = LoggerFactory.getLogger(Librarian.class);
	
	private BookShelf shelf;
	private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

	 Member(BookShelf bookShelf, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
	    this.shelf = bookShelf;
		this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
	  }


	// add, update, and remove Books from the system 
	 
	@GetMapping("/member/getBooks")
	List<Book> all() {
      return shelf.findAll();
	}
	
	@GetMapping("/member/getBook/{name}")
	List<Book> findByName(@PathVariable String name) {
		return shelf.findByName(name);
	}

	@PostMapping("/member/borrowBook/id={idStr}")
	public String borrowBook(@PathVariable String idStr) {
		long id = Long.parseLong(idStr);  
		String ret = "Book Not Found.";		
		List<Book> bookList = shelf.findById(id);
		if(bookList != null && bookList.size() > 0) {
			Book book = bookList.get(0);
			if(book.getStatus() == Book.Status.AVAILABLE) {
				book.setStatus(Book.Status.BORROWED);
				shelf.save(book);
				ret = "Borrowed book:" + book.getName();
			}else {
				ret = "Book is currently not available:" + book.getName();								
			}			
		}		
		
		return ret;
	}
	
	@PostMapping("/member/returnBook/id={idStr}")
	public String returnBook(@PathVariable String idStr) {
		long id = Long.parseLong(idStr);  
		String ret = "Book Not Found.";		
		List<Book> bookList = shelf.findById(id);
		if(bookList != null && bookList.size() > 0) {
			Book book = bookList.get(0);
			if(book.getStatus() == Book.Status.BORROWED) {
				book.setStatus(Book.Status.AVAILABLE);
				shelf.save(book);
				ret = "Returned book:" + book.getName();
			}else {
				ret = "Book is already returned:" + book.getName();								
			}
		}		
		
		return ret;
	}	

	
	@PostMapping("/member/remMember")
	public String removeMember() {
		String ret = "Failed to remove member.";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		if (inMemoryUserDetailsManager.userExists(username)) {
			inMemoryUserDetailsManager.deleteUser(username);			
			ret = "Removed member:" + username;
		}else {
			ret = "Member not exists:" + username;
		}			
		return ret;
	}

}
