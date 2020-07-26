package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentalService {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;

	public void borrowBook(Long bookId, Long userId) {
		
	}
	
	public void returnBook(Long bookId) {
		
	}

}
