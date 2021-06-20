package posmy.interview.boot.book;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/public")
public class BookController {
	
	private final BookService bookService;
	
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	// public API
	
	@GetMapping("/book")
	public @ResponseBody ResponseEntity<List<Book>> getAllBooks(
			@RequestParam long offset,
			@RequestParam long limit) {
		
		return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
	}
}
