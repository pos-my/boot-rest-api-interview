package posmy.interview.boot.service;

import java.util.List;

import org.springframework.stereotype.Component;

import posmy.interview.boot.Book;
import posmy.interview.boot.User;

@Component
public interface SystemService {

	User findByUsername(String username);

	User findRoleUser(String username);

	List<Book> findBookAvailable();

	User findUserByRole(int value);

	Book addBook(Book reqBook);

	void updateBook(Book reqBook);

	void removeBook(Book reqBook);

	Book findBookStatus(Book reqBook);

	User addMember(User reqUser);

	void updateMember(User reqUser);

	void removeMember(User reqUser);

	List<Book> findBookUserId(int id);

	void borrowBook(List<Book> book, int id);

	void returnBook(List<Book> bookReq);

	void deleteAcc(int id);

}
