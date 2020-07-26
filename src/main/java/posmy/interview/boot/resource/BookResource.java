package posmy.interview.boot.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.istack.internal.NotNull;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.service.BookService;

@Controller
@RequestMapping("books")
public class BookResource {
	
	@Autowired
	private BookService bookService;
	
	@PostMapping("/new")
	public String createNewBook(@NotNull @ModelAttribute("book") BookDto bookDto) {
		bookService.saveBook(bookDto);
		return "redirect:/view-books";
	}
	
	@PostMapping("/update")
	public String updateBook(@NotNull @ModelAttribute("book") BookDto bookDto) {
		bookService.updateBook(bookDto);
		return "redirect:/view-books";	
	}
	
	@PostMapping("/delete/{id}")
	public String deleteBook(@NotNull @PathVariable Long id) {
		bookService.deleteBookById(id);
		return "redirect:/view-books";
	}
	
	@PostMapping("/borrow/{id}")
	public String updateBook(@NotNull @PathVariable Long id) {
		bookService.borrowBook(id);
		return "redirect:/view-books";	
	}
	
	@PostMapping("/return/{id}")
	public String returnBook(@NotNull @PathVariable Long id) {
		bookService.returnBook(id);
		return "redirect:/view-books";	
	}
	
}
