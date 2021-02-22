package posmy.interview.boot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.config.TokenProvider;
import posmy.interview.boot.dao.BookDao;
import posmy.interview.boot.dao.UserDao;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;


@RestController
@RequestMapping("/mem")
public class MemberController {

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@Autowired
	private BookDao dao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private TokenProvider jwtTokenUtil;



	@GetMapping("/books")
	public List<Book> getAllBooks() {
		return bookService.findAll();
	}



	@PutMapping("/borrowBook/{id}")
	public ResponseEntity<?> borrowBook(@PathVariable(value="id") Long id, HttpServletRequest request) throws Exception{

		String token = request.getHeader("Authorization");	

		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			token=  token.substring(7, token.length());
		}

		String sessionUsername = jwtTokenUtil.getUserNameFromJwtToken(token);
		User user = userService.findOne(sessionUsername);


		return bookService.borrowBook(id, user);

	}

	@PutMapping("/returnBook/{id}")
	public ResponseEntity<?> returnBook(@PathVariable(value="id") Long id, HttpServletRequest request) throws Exception{

		String token = request.getHeader("Authorization");	

		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			token=  token.substring(7, token.length());
		}

		String sessionUsername = jwtTokenUtil.getUserNameFromJwtToken(token);
		User user = userService.findOne(sessionUsername);

		return bookService.returnBook(id, user);

	}

	@DeleteMapping("/deleteAccount")
	public Map<String, Boolean> selfDelete(HttpServletRequest request) throws Exception{

		Map<String, Boolean> mapResponse = new HashMap<>();

		String token = request.getHeader("Authorization");


		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			token=  token.substring(7, token.length());
		}

		String sessionUsername = jwtTokenUtil.getUserNameFromJwtToken(token);	
		User user = userService.findOne(sessionUsername);


		if(user.getBook() != null && user.getBook().size()>0) {
			mapResponse.put("Madam Irma would be mad you did not return the book before deleting your account, Potter.", Boolean.TRUE);
			return mapResponse;

		}

		userDao.delete(user);
		mapResponse.put("deleted", Boolean.TRUE);
		return mapResponse;

	}	

}

