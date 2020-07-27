package posmy.interview.boot.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.model.Role.RoleName;
import posmy.interview.boot.model.User;
import posmy.interview.boot.security.MyUserDetails;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

@Controller
public class AppResource {

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@GetMapping()
	public ModelAndView viewHomePage(Model model) {
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}

	@GetMapping("/view-books")
	public ModelAndView viewBookPage(Model model) {
		List<BookDto> books = bookService.getAllBooks();
		ModelAndView mav = new ModelAndView("books");
		model.addAttribute("listBooks", books);
		return mav;
	}

	@GetMapping("/create-book")
	public ModelAndView createNewBook() {
		ModelAndView mav = new ModelAndView("new_book");
		BookDto book = new BookDto();
		mav.addObject("book", book);
		return mav;
	}

	@GetMapping("/update-book/{id}")
	public ModelAndView getBookForUpdate(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView("update_book");
		BookDto book = bookService.getBookById(id);
		mav.addObject("book", book);
		return mav;
	}

	@GetMapping("/view-users")
	public ModelAndView viewUserPage(Model model) {
		List<UserDto> users = new ArrayList<>();

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		Object user = loggedInUser.getPrincipal();

		if (user instanceof MyUserDetails) {
			User currentUser = ((MyUserDetails) user).getUser();
			boolean isLibrarian = RoleName.LIBRARIAN.equals(currentUser.getRole().getName());

			if (isLibrarian) {
				users = userService.getAllUsers();
			} else {
				UserDto userDto = UserDto.builder().id(currentUser.getId()).name(currentUser.getName())
						.username(currentUser.getUsername()).role(currentUser.getRole())
						.enabled(currentUser.isEnabled()).build();
				users.add(userDto);
			}
		}

		ModelAndView mav = new ModelAndView("users");
		model.addAttribute("listUsers", users);
		return mav;
	}
	
	@GetMapping("/create-user")
	public ModelAndView createNewUser() {
		ModelAndView mav = new ModelAndView("new_user");
		UserDto user = new UserDto();
		mav.addObject("user", user);
		return mav;
	}
	
	@GetMapping("/update-user/{id}")
	public ModelAndView getUserForUpdate(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView("update_user");
		UserDto user = userService.getUserById(id);
		mav.addObject("user", user);
		return mav;
	}
}
