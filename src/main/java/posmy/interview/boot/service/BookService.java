package posmy.interview.boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.model.requestModel.BookRegisterRequest;

public interface BookService {

	public static final String BOR = "BORROWED";
	public static final String AVA = "AVAILABLE";


	List<Book> findAll();	
	Optional<Book> findById(Long id);	
	Optional<Book> findByCode(String code);	
	Optional<Book> findByName(String name);
	void saveBook(BookRegisterRequest bookReq);
	ResponseEntity<?> borrowBook(Long id, User user);
	ResponseEntity<?> returnBook(Long id, User user);
}

