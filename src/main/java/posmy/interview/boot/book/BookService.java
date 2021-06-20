package posmy.interview.boot.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import posmy.interview.boot.entities.BookBorrowRecord;
import posmy.interview.boot.entities.BookBorrowRecordRepository;
import posmy.interview.boot.entities.BookRepository;
import posmy.interview.boot.entities.MemberRepository;
import posmy.interview.boot.member.Member;

@Service
public class BookService {
	
	private final BookRepository bookRepository;
	private final BookBorrowRecordRepository bookBorrowRepository;
	private final MemberRepository memberRepository;

	public BookService(BookRepository bookRepository, BookBorrowRecordRepository bookBorrowRepository, MemberRepository memberRepository) {
		this.bookRepository = bookRepository;
		this.bookBorrowRepository = bookBorrowRepository;
		this.memberRepository = memberRepository;
	}

	/**
	 * Get all books available in DB
	 * @return
	 */
	public List<Book> getAllBooks() {
		List<Book> results = new ArrayList<>();
		for(posmy.interview.boot.entities.Book bookDb : bookRepository.findAll()) {
			results.add(new Book(bookDb.getIsbn(), bookDb.getTitle())); 
		}
		return results;
//		return bookRepository.findAll().stream().map(bookDb -> {
//			return new Book(bookDb.getIsbn(), bookDb.getTitle()); 
//		}).collect(Collectors.toList());
	}

	/**
	 * Add new book entry to DB. 
	 * @param books
	 * @return true if success insert, false otherwise
	 */
	public boolean addBooks(List<Book> books) {
		if(books.stream().allMatch(book -> bookRepository.findById(book.getIsbn()).isPresent())) {
			return false;
		}
		books.forEach(book -> {
			posmy.interview.boot.entities.Book bookDb = new posmy.interview.boot.entities.Book();
			bookDb.setIsbn(book.getIsbn());
			bookDb.setTitle(book.getTitle());
			bookRepository.saveAndFlush(bookDb);
		});
		return true;
	}

	/** 
	 * Update book by given ISBN
	 * @param isbn
	 * @param book
	 * @return
	 */
	public boolean updateBook(Long isbn, Book book) {
		if(!bookRepository.findById(isbn).isPresent()) {
			return false;
		}
		posmy.interview.boot.entities.Book bookDb = new posmy.interview.boot.entities.Book();
		bookDb.setIsbn(isbn);
		bookDb.setTitle(book.getTitle());
		bookRepository.saveAndFlush(bookDb);
		return true;
	}

	public boolean deleteBook(Long isbn) {
		if(!bookRepository.findById(isbn).isPresent()) {
			return false;
		}
		bookRepository.deleteById(isbn);
		return true;
	}
	
	public boolean borrowBook(Long isbn, Long memberId) {
		Optional<posmy.interview.boot.entities.Book> bookInDb = bookRepository.findById(isbn);
		if(!bookInDb.isPresent()) {
			return false;
		}
		Optional<posmy.interview.boot.entities.Member> memberToDb = memberRepository.findById(memberId);
		if(!memberToDb.isPresent()) {
			return false;
		}
		bookInDb.get().setStatus(posmy.interview.boot.entities.Book.Status.BORROWED);
		
		BookBorrowRecord bookBorrow = new BookBorrowRecord();
		bookBorrow.setBook(bookInDb.get());
		bookBorrow.setMember(memberToDb.get());
		bookBorrow.setAction(BookBorrowRecord.Action.BORROW);
		bookBorrowRepository.saveAndFlush(bookBorrow);

		bookRepository.saveAndFlush(bookInDb.get());
		return true;
	}

	public boolean returnBook(Long isbn, Long memberId) {
		Optional<posmy.interview.boot.entities.Book> bookInDb = bookRepository.findById(isbn);
		if(!bookInDb.isPresent()) {
			return false;
		}
		Optional<posmy.interview.boot.entities.Member> memberInDb = memberRepository.findById(memberId);
		if(!memberInDb.isPresent()) {
			return false;
		}
		Optional<BookBorrowRecord> bookBorrowRecordInDb = bookBorrowRepository
				.findByBookIsbnAndMemberIdAndAction(isbn, memberId, BookBorrowRecord.Action.BORROW);
		if(!bookBorrowRecordInDb.isPresent()) {
			return false;
		}
		bookInDb.get().setStatus(posmy.interview.boot.entities.Book.Status.AVAILABLE);
		
		BookBorrowRecord bookBorrow = new BookBorrowRecord();
		bookBorrow.setBook(bookInDb.get());
		bookBorrow.setMember(memberInDb.get());
		bookBorrow.setAction(BookBorrowRecord.Action.RETURN);
		bookBorrowRepository.saveAndFlush(bookBorrow);
		
		bookRepository.saveAndFlush(bookInDb.get());
		return true;
	}

}
