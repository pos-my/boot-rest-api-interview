package posmy.interview.boot.api;

import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import posmy.interview.boot.constant.ErrorEnum;
import posmy.interview.boot.constant.LibraryConstant;
import posmy.interview.boot.exception.WebserviceException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.MemberService;
import posmy.interview.boot.view.BookView;

/**
 * @author Aboy
 * @version 0.01
 */
@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BookService bookService;

	@Autowired
	private MemberService memberService;

	/**
	 * Fetch a list of book
	 * @return a list of book
	 * @throws Exception 
	 */
	@RequestMapping(path="/allBooks", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Fetch all book")
	public ResponseEntity<?> book() throws Exception {
		List<Book> book = (List<Book>) bookService.findAllBooks();

		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	/**
	 * Finds a book by <code>bookCode</code>
	 * 
	 * @param bookCode book's bookCode
	 * 
	 * @return the {@link Book} object
	 * @throws Exception 
	 */
	@RequestMapping(path = "/getBookByBookCode/{bookCode}", 
			method = RequestMethod.GET)
	@ApiOperation(value = "Fetch a book")
	public ResponseEntity<?> book(@PathVariable String bookCode) throws Exception {
		Book book = new Book();
		book = bookService.findByBookCode(bookCode);

		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	/**
	 * Add a book
	 * 
	 * @param bookView
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/newBook",
			method = RequestMethod.POST,
			consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a new book")
	public ResponseEntity<Book> addBook(@RequestBody BookView bookView) throws Exception {

		Book book = new Book();
		Book savedBook = new Book();

		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<BookView>> violations = validator.validate(bookView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<BookView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//set view to model to be saved
			book.setBookCode(bookView.getBookCode());
			book.setTitle(bookView.getTitle());
			book.setAuthor(bookView.getAuthor());
			book.setStatus(LibraryConstant.BOOK_AVAILABLE);
			book.setOwnedBy(LibraryConstant.OWNER);

			try {
				logger.info("Saving Book");
				savedBook =  bookService.saveBook(book);
				logger.info("Book creation completed");
			}catch (Exception e) {
				logger.error("Error:- Unable to create book");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		return new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
	}

	/**
	 * Updates the book
	 * 
	 * @param updateBookView
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/updateBook/{bookCode}",
			method = RequestMethod.PUT)
	@ApiOperation(value = "Update a book")
	public ResponseEntity<Book> updateBook(@PathVariable String bookCode, @RequestBody BookView updateBookView) throws Exception {

		Book savedBook = new Book();
		logger.info("Book no: "+bookCode);
		Book bookFindByBookCode = bookService.findByBookCode(bookCode);
		logger.info("Selected bookCode: "+bookFindByBookCode.getBookCode());
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<BookView>> violations = validator.validate(updateBookView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<BookView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			logger.info("Selected Book code: "+bookFindByBookCode.getBookCode());
			bookFindByBookCode.setBookCode(updateBookView.getBookCode());
			bookFindByBookCode.setTitle(updateBookView.getTitle());
			bookFindByBookCode.setAuthor(updateBookView.getAuthor());

			try {
				logger.info("Update Book");
				savedBook = bookService.updateBook(bookFindByBookCode);
				logger.info("Book updated");
			}catch (Exception e) {
				logger.error("Error:- Unable to update book");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}

		return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
	}


	/**
	 * Deletes book identified with <code>bookCode</code>
	 * @param bookCode
	 * @throws Exception 
	 */
	@RequestMapping(path = "/removeBook/{bookCode}", 
			method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete a book")
	public ResponseEntity<?> deleteBook(@PathVariable String bookCode) throws Exception {

		Book bookFindByBookCode = bookService.findByBookCode(bookCode);
		try {
			logger.info("Delete Book");
			bookService.deleteBook(bookFindByBookCode);
			logger.info("Book Deleted");
		}catch (Exception e) {
			logger.error("Error:- Unable to delete book");
			throw new WebserviceException(ErrorEnum.DELETION_UNSUCCESSFUL, ErrorEnum.DELETION_UNSUCCESSFUL.getDescription());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Update the book status
	 * 
	 * @param updateBookView
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/BookStatus/{bookCode}/{borrowerId}/{status}",
			method = RequestMethod.PUT)
	@ApiOperation(value = "Update book status")
	public ResponseEntity<Book> updateBookStatus(@PathVariable String bookCode, @PathVariable String borrowerId, @PathVariable String status) throws Exception {

		Book savedBook = new Book();
		logger.info("Book no: "+bookCode);
		Book bookFindByBookCode = bookService.findByBookCode(bookCode);
		logger.info("Selected bookCode: "+bookFindByBookCode.getBookCode());

		Member borrower = memberService.findByMemberId(borrowerId);
		logger.info("Member ID: "+borrower.getMemberId() + ", Borrower name: "+borrower.getFullname());

		if (status.equalsIgnoreCase(LibraryConstant.BOOK_BORROWED)) {
			bookFindByBookCode.setOnLoanTo(borrower);
		}
		if (status.equalsIgnoreCase(LibraryConstant.BOOK_AVAILABLE)) {
			bookFindByBookCode.setOnLoanTo(null);
		}
		bookFindByBookCode.setStatus(status);

		try {
			logger.info("Update Book");
			savedBook = bookService.updateBook(bookFindByBookCode);
			logger.info("Book updated");
		}catch (Exception e) {
			logger.error("Error:- Unable to update book");
			throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
		}
		return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
	}
}
