package posmy.interview.boot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import posmy.interview.boot.constant.BookStatus;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.rest.BookDetail;
import posmy.interview.boot.model.rest.BookRequest;
import posmy.interview.boot.model.rest.BookResponse;
import posmy.interview.boot.model.rest.GetBookRequest;
import posmy.interview.boot.model.rest.GetBookResponse;
import posmy.interview.boot.respository.BookRepository;

@Service
public class BookServiceImpl implements BookService{

	private BookRepository bookRepository;
	
	public BookServiceImpl(BookRepository bookRepository) {
		
		this.bookRepository = bookRepository;
	}
	
	@Override
	public GetBookResponse getAllBooks() {
		//Initialize rest response 
		GetBookResponse response = new GetBookResponse();
		List<Book> bookList = new ArrayList<>();
		List<BookDetail> bookDetailList = new ArrayList<>();
		
		//Get all Book Data
		bookList = bookRepository.findAll();
		
		//Set Book Data to rest response
		for(Book book: bookList) {
			
			BookDetail bookDetail = new BookDetail();
			
			bookDetail.setBookId(book.getBookId());
			bookDetail.setBookName(book.getBookName());
			bookDetail.setStatus(book.getStatus());
			
			bookDetailList.add(bookDetail);
			
		}
		
		response.setBook(bookDetailList);

		return response;
	}

	@Override
	public BookResponse getBooksById(BookRequest request) throws Exception{
		//Initialize rest response 
		BookResponse response = new BookResponse();
		BookDetail bookDetail = new BookDetail();
		
		//Get Book Data using book id
		Book book = bookRepository.findByBookId(request.getId());
		
		//throw exception if book id not found 
		if(book == null) {
			throw new Exception("Book Id not found");
		}
		
		bookDetail.setBookId(book.getBookId());
		bookDetail.setBookName(book.getBookName());
		bookDetail.setStatus(book.getStatus());
			
		response.setBookDetail(bookDetail);
		
		return response;	
	}
	
	@Override
	public BookResponse addBooks(BookRequest request) throws Exception {
		//Initialize rest response
		BookResponse response = new BookResponse();
		
		//Initialize book entity
		Book book = new Book();
		
		//Check if the book id exist 
		Book searchBook = bookRepository.findByBookId(request.getId());
		
		//throw exception if book exist
		if(searchBook != null) {
			
			throw new Exception("Book already exist");
		}
		
		book.setBookId(request.getId());
		book.setBookName(request.getBookName());
		book.setStatus(BookStatus.AVAILABLE.name());
		
		Book responseBook = bookRepository.save(book);
		
		BookDetail bookDetail = new BookDetail();
		bookDetail.setBookId(responseBook.getBookId());
		bookDetail.setBookName(responseBook.getBookName());
		bookDetail.setStatus(responseBook.getStatus());
		
		response.setBookDetail(bookDetail);
		
		return response;
		
	}

	@Override
	public BookResponse updateBooks(BookRequest request) throws Exception {
		//Initialize rest response
		BookResponse response = new BookResponse();
		
		//Check if Book exist
		Book targetBook = bookRepository.findByBookId(request.getId());
		
		//throw exception if book not exist
		if(targetBook == null) {
			throw new Exception("Book Id not found");
		}
		
		targetBook.setBookName(request.getBookName());
		targetBook.setStatus(request.getStatus());
		
		Book responseBook = bookRepository.save(targetBook);
		
		BookDetail bookDetail = new BookDetail();
		bookDetail.setBookId(responseBook.getBookId());
		bookDetail.setBookName(responseBook.getBookName());
		bookDetail.setStatus(responseBook.getStatus());
		
		response.setBookDetail(bookDetail);
		
		return response;
		
	}

	@Override
	public String removeBooks(GetBookRequest request) throws Exception {
		//Check if Book exist
		Book book = bookRepository.findByBookId(request.getId());
		
		//throw exception if book doesnt exist
		if(book == null) {
			throw new Exception("Book Id not found");
		}
		
		
		bookRepository.deleteById(book.getBookId());
		
		return "Delete Successfully";
	}

	@Override
	public BookResponse borrowBooks(BookRequest request) throws Exception {
		//Initialize rest response
		BookResponse response = new BookResponse();
		
		Book book = new Book();
		
		book = bookRepository.findByBookId(request.getId());
		
		if(book == null) {
			
			throw new Exception("Book Id not found");
		}
		
		book.setStatus(BookStatus.BORROWED.name());
			
		Book responseBook = bookRepository.save(book);
		
		BookDetail bookDetail = new BookDetail();
		bookDetail.setBookId(responseBook.getBookId());
		bookDetail.setBookName(responseBook.getBookName());
		bookDetail.setStatus(responseBook.getStatus());
		
		response.setBookDetail(bookDetail);
		
		return response;
		
	}


	@Override
	public BookResponse returnBooks(BookRequest request) throws Exception {
		//Initialize rest response
		BookResponse response = new BookResponse();
		BookDetail bookDetail = new BookDetail();
		
		Book book = new Book();
		
		//Check if book exist 
		book = bookRepository.findByBookId(request.getId());
		
		//throw exception if book not exist
		if(book == null) {
			
			throw new Exception("Book Id not found");
		}
		
		book.setStatus(BookStatus.AVAILABLE.name());
			
		Book responseBook = bookRepository.save(book);
		
		bookDetail.setBookId(responseBook.getBookId());
		bookDetail.setBookName(responseBook.getBookName());
		bookDetail.setStatus(responseBook.getStatus());
		
		response.setBookDetail(bookDetail);
		
		return response;
	}




}
