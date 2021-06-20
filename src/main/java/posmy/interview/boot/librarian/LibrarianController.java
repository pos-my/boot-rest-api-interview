package posmy.interview.boot.librarian;

import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.book.Book;
import posmy.interview.boot.member.Member;

@Controller
@RequestMapping("/api/librarian")
public class LibrarianController {
	
	private final LibrarianService librarianService;
	
	public LibrarianController(LibrarianService librarianService) {
		this.librarianService = librarianService;
	}
	
	// Book related
	
	@PostMapping("/book")
	public ResponseEntity<Boolean> addBooks(List<Book> books) {
		boolean isSuccess = librarianService.addBooks(books);
		if(!isSuccess) {
			return new ResponseEntity<>(isSuccess, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(isSuccess, HttpStatus.OK);
	}
	
	@PutMapping("/book/{isbn}")
	public ResponseEntity<Boolean> updateBookByIsbn(@PathParam("isbn") Long isbn, Book book) {
		boolean isSuccess = librarianService.updateBook(isbn, book);
		if(!isSuccess) {
			return new ResponseEntity<>(isSuccess, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(isSuccess, HttpStatus.OK);
	}
	
	@DeleteMapping("/book/{isbn}")
	public ResponseEntity<Boolean> deleteBookByIsbn(@PathParam("isbn") Long isbn) {
		boolean isSuccess = librarianService.deleteBook(isbn);
		if(!isSuccess) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(isSuccess, HttpStatus.OK);
	}
	
	// Member related
	
	@PostMapping("/member")
	public ResponseEntity<Boolean> addMembers(List<Member> members) {
		boolean isSuccess = librarianService.addMembers(members);
		if(!isSuccess) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(isSuccess, HttpStatus.OK);
	}
	
	@PutMapping("/member/{id}")
	public ResponseEntity<Boolean> updateMemberById(@PathParam("id") Long id, Member member) {
		boolean isSuccess = librarianService.updateMember(id, member);
		if(!isSuccess) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(isSuccess, HttpStatus.OK);
	}
	
	@GetMapping("/member/{id}")
	public ResponseEntity<Member> viewMemberById(@PathParam("id") Long id) {
		Optional<Member> member = librarianService.getMember(id);
		if(!member.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(member.get(), HttpStatus.OK);
	}
	
	@DeleteMapping("/member/{id}")
	public ResponseEntity<Boolean> deleteMemberById(@PathParam("id") Long id) {
		boolean isSuccess = librarianService.deleteMember(id);
		if(!isSuccess) {
			return new ResponseEntity<>(isSuccess, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(isSuccess, HttpStatus.OK);
	}
}
