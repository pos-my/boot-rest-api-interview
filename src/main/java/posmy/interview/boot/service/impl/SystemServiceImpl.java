package posmy.interview.boot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.Book;
import posmy.interview.boot.BookStatus;
import posmy.interview.boot.User;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.service.SystemService;

@Service
public class SystemServiceImpl implements SystemService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findRoleUser(String username) {
		return userRepository.findRoleUser(username);
	}

	@Override
	public List<Book> findBookAvailable() {
//		List<Book> book = bookRepository.findBookAvailable(BookStatus.AVAILABLE.getLabel());
//		for (Book b : book) {
//			User user = new User();
//			b.setUser(user);
//		}
		return bookRepository.findBookAvailable(BookStatus.AVAILABLE.getLabel());
	}

	@Override
	public User findUserByRole(int roleId) {
		return userRepository.findUserByRole(roleId);
	}

	@Override
	public Book addBook(Book reqBook) {
		Book b = new Book();
		b.setName(reqBook.getName());
		b.setType(reqBook.getType());
		b.setStatus(BookStatus.AVAILABLE.getLabel());
		b.setUser(null);
		return bookRepository.save(reqBook);
	}

	@Override
	public void updateBook(Book reqBook) {
		bookRepository.updateBook(reqBook.getId(), reqBook.getName(), reqBook.getType(), reqBook.getStatus(),
				!(reqBook.getUser() == null) ? reqBook.getUser().getId() : null);
	}

	@Override
	public void removeBook(Book reqBook) {
		bookRepository.delete(reqBook);
	}

	@Override
	public Book findBookStatus(Book reqBook) {
		return bookRepository.findBookStatus(reqBook.getId(), BookStatus.AVAILABLE.getLabel());
	}

	@Override
	public User addMember(User reqUser) {
		User u = new User();
		u.setUsername(reqUser.getUsername());
		u.setRoleId(reqUser.getRoleId());
		u.setDeleted(false);
		return userRepository.save(u);
	}

	@Override
	public void updateMember(User reqUser) {
		User u = new User();
		u.setUsername(reqUser.getUsername());
		u.setRoleId(reqUser.getRoleId());
		u.setDeleted(reqUser.isDeleted());
		userRepository.updateMember(reqUser.getId(), u.getUsername(), u.getRoleId(), u.isDeleted());
	}

	@Override
	public void removeMember(User reqUser) {
		userRepository.delete(reqUser);
	}

	@Override
	public List<Book> findBookUserId(int userId) {
		return bookRepository.findBookUserId(userId);
	}

	@Override
	public void borrowBook(List<Book> book, int userId) {
		List<String> ls = new ArrayList<String>();
		String a = null;
		
		for(Book b : book) {
			int i = 1;
			a = b.getId() + (i == book.size() ? "" :", ");
			i++;
			ls.add(a);
		}
		bookRepository.borrowBook(ls, userId, BookStatus.BORROWED.getLabel());
	}

	@Override
	public void returnBook(List<Book> bookReq) {
		List<String> ls = new ArrayList<String>();
		String a = null;
		
		for(Book b : bookReq) {
			int i = 1;
			a = b.getId() + (i == bookReq.size() ? "" :", ");
			i++;
			ls.add(a);
		}
		
		bookRepository.returnBook(ls, BookStatus.AVAILABLE.getLabel());
	}

	@Override
	public void deleteAcc(int userId) {
		userRepository.deleteAcc(userId, Boolean.TRUE);
	}

}
