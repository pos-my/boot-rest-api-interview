package posmy.interview.boot.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.entity.Book;
import posmy.interview.boot.object.BookObject;
import posmy.interview.boot.repository.IBookRepository;
import posmy.interview.boot.service.IBookService;

@Service
public class BookServiceImpl implements IBookService{
	
	@Autowired
	IBookRepository bookRepo;
	
	@Override
	public Optional<Book> create(BookObject bookObject) {
		Book bookEntity = new Book();
		bookEntity.setStatus("AVAILABLE");
		bookEntity.setTitle(bookObject.getTitle());
		bookRepo.save(bookEntity);
		
		return Optional.of(bookEntity);
	}

	@Override
	public Optional<Book> update(BookObject bookObject, Long bookId) {
		Optional<Book> bookOptional = bookRepo.findById(bookId);
		Book bookEntity = bookOptional.get();

		bookEntity.setTitle(bookObject.getTitle());
		
		bookRepo.save(bookEntity);
		
		return Optional.of(bookEntity);
	}

	@Override
	public Optional<Boolean> delete(Long bookId) {
		Book bookEntity = new Book();
		Optional<Book> tempBook = this.findByBookId(bookId);
		
		bookEntity = tempBook.get();
		bookEntity.setDeleted(true);
		
		bookRepo.save(bookEntity);
		
		return Optional.of(bookEntity.isDeleted());
	}

	@Override
	public List<Book> findAll() {
		List<Book> bookList = bookRepo.findAll();
		
		return bookList;
	}

	@Override
	public Optional<Book> findByBookId(Long bookId) {
		Optional<Book> optionalBook = bookRepo.findById(bookId);
		
		return optionalBook;
	}

	@Override
	public Optional<Book> borrow(Long bookId) {
		Book bookEntity = new Book();
		Optional<Book> tempBook = this.findByBookId(bookId);
		
		bookEntity = tempBook.get();
		bookEntity.setStatus("BORROWED");
		bookRepo.save(bookEntity);
		
		return Optional.of(bookEntity);
	}

	@Override
	public Optional<Book> returnBook(Long bookId) {
		Book bookEntity = new Book();
		Optional<Book> tempBook = this.findByBookId(bookId);
		
		bookEntity = tempBook.get();
		bookEntity.setStatus("AVAILABLE");
		bookRepo.save(bookEntity);
		
		return Optional.of(bookEntity);
	}
	
}
