package posmy.interview.boot.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.dao.Book;
import posmy.interview.boot.dao.Users;
import posmy.interview.boot.dao.repo.BookRepo;
import posmy.interview.boot.dao.repo.UsersRepo;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.enums.ErrorCode;
import posmy.interview.boot.enums.ResponseStatus;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.model.BaseResponse;
import posmy.interview.boot.model.librarian.BookBean;
import posmy.interview.boot.model.member.ViewBooksRes;

@Service
public class MemberServiceImpl implements MemberService{
  private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

  @Autowired
  UsersRepo usersRepo;
  
  @Autowired
  BookRepo bookRepo;
  
  @Override
  public ViewBooksRes viewBooks(String authHeader) {
    ViewBooksRes response = new ViewBooksRes();

    if (authHeader.isEmpty()) {
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.BAD_REQUEST.getCode());
      response.setErrMsg(ErrorCode.BAD_REQUEST.getDescription());
      return response;
    }

    Users user = usersRepo.findByUsrIdIgnoreCase(authHeader);
    logger.info("Checking user role...");

    if (user == null) {
      logger.info("User ({}) not found", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.INVALID_ACCOUNT.getCode());
      response.setErrMsg(ErrorCode.INVALID_ACCOUNT.getDescription());
      return response;

    } else if (user.getRole().equalsIgnoreCase(UserRole.MEMBER.getCode()) == false) {
      logger.info("User ({}) does not have permission to view books", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }
    
    List<Book> bookList = bookRepo.findAll();
    List<BookBean> resultList = new ArrayList<BookBean>();
    
    for(Book book: bookList) {
      BookBean bookObj = new BookBean(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getYear(), book.getStatus(), book.getBorrowedBy());
      resultList.add(bookObj);
    }
    
    response.setStatus(ResponseStatus.SUCCESS.getCode());
    response.setBookList(resultList);
    return response;
  }

  @Override
  public BaseResponse borrowBooks(String authHeader, List<String> isbnList) {
    BaseResponse response = new BaseResponse();

    if (authHeader.isEmpty()) {
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.BAD_REQUEST.getCode());
      response.setErrMsg(ErrorCode.BAD_REQUEST.getDescription());
      return response;
    }

    Users user = usersRepo.findByUsrIdIgnoreCase(authHeader);
    logger.info("Checking user role...");

    if (user == null) {
      logger.info("User ({}) not found", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.INVALID_ACCOUNT.getCode());
      response.setErrMsg(ErrorCode.INVALID_ACCOUNT.getDescription());
      return response;

    } else if (user.getRole().equalsIgnoreCase(UserRole.MEMBER.getCode()) == false) {
      logger.info("User ({}) does not have permission to borrow books", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }
    
    List<Book> bookList = new ArrayList<Book>(); 
    
    for(String isbn: isbnList) {
      Book book = bookRepo.findByIsbn(isbn);
      if(book.getStatus().equalsIgnoreCase(BookStatus.AVAILABLE.getCode())) {
        book.setStatus(BookStatus.BORROWED.getCode());
        book.setBorrowedBy(authHeader);
        bookList.add(book);
      } else {
        logger.info("Book with isbn({}) is not available for borrowing", isbn);
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setErrCode(ErrorCode.BOOK_NOT_AVAILABLE.getCode());
        response.setErrMsg(ErrorCode.BOOK_NOT_AVAILABLE.getDescription());
        return response;
      }      
    }
    
    try {
      logger.info("{} book borrow requests received, borrowing {} books...", isbnList.size(), bookList.size());
      bookRepo.saveAll(bookList);
      logger.info("Borrowed books successfully");

      response.setStatus(ResponseStatus.SUCCESS.getCode());
      return response;
    } catch (Exception e) {
      logger.error("Exception found when borrowing books", e);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.EXCEPTION_FOUND.getCode());
      response.setErrMsg(ErrorCode.EXCEPTION_FOUND.getCode() + " | " + e.getMessage());
      return response;
    }
  }

  @Override
  public BaseResponse returnBooks(String authHeader, List<String> isbnList) {
    BaseResponse response = new BaseResponse();

    if (authHeader.isEmpty()) {
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.BAD_REQUEST.getCode());
      response.setErrMsg(ErrorCode.BAD_REQUEST.getDescription());
      return response;
    }

    Users user = usersRepo.findByUsrIdIgnoreCase(authHeader);
    logger.info("Checking user role...");

    if (user == null) {
      logger.info("User ({}) not found", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.INVALID_ACCOUNT.getCode());
      response.setErrMsg(ErrorCode.INVALID_ACCOUNT.getDescription());
      return response;

    } else if (user.getRole().equalsIgnoreCase(UserRole.MEMBER.getCode()) == false) {
      logger.info("User ({}) does not have permission to return books", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }
    
    List<Book> bookList = new ArrayList<Book>(); 
    
    for(String isbn: isbnList) {
      Book book = bookRepo.findByIsbn(isbn);
      if(book.getStatus().equalsIgnoreCase(BookStatus.BORROWED.getCode()) 
          && book.getBorrowedBy().equalsIgnoreCase(authHeader)) {
        book.setStatus(BookStatus.AVAILABLE.getCode());
        book.setBorrowedBy(null);
        bookList.add(book);
      } else {
        logger.info("Book with isbn({}) not able to be returned as it is not borrowed by user({})", isbn, authHeader);
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setErrCode(ErrorCode.BOOK_NOT_BORROWED_BY_USER.getCode());
        response.setErrMsg(ErrorCode.BOOK_NOT_BORROWED_BY_USER.getDescription());
        return response;
      }      
    }
    
    try {
      logger.info("{} book return requests received, returning {} books...", isbnList.size(), bookList.size());
      bookRepo.saveAll(bookList);
      logger.info("Returned books successfully");

      response.setStatus(ResponseStatus.SUCCESS.getCode());
      return response;
    } catch (Exception e) {
      logger.error("Exception found when returning books", e);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.EXCEPTION_FOUND.getCode());
      response.setErrMsg(ErrorCode.EXCEPTION_FOUND.getCode() + " | " + e.getMessage());
      return response;
    }
  }

  @Override
  public BaseResponse deleteAccount(String authHeader) {
    BaseResponse response = new BaseResponse();

    if (authHeader.isEmpty()) {
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.BAD_REQUEST.getCode());
      response.setErrMsg(ErrorCode.BAD_REQUEST.getDescription());
      return response;
    }

    Users user = usersRepo.findByUsrIdIgnoreCase(authHeader);
    logger.info("Checking user role...");

    if (user == null) {
      logger.info("User ({}) not found", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.INVALID_ACCOUNT.getCode());
      response.setErrMsg(ErrorCode.INVALID_ACCOUNT.getDescription());
      return response;

    } else if (user.getRole().equalsIgnoreCase(UserRole.MEMBER.getCode()) == false) {
      logger.info("User ({}) does not have permission to delete account", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }
    
    try {
      logger.info("Deleting account for user ({})...", authHeader);
      usersRepo.delete(user);
      logger.info("Deleted account successfully");
      
      response.setStatus(ResponseStatus.SUCCESS.getCode());
      return response;
    } catch (Exception e) {
      logger.error("Exception found when deleting account", e);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.EXCEPTION_FOUND.getCode());
      response.setErrMsg(ErrorCode.EXCEPTION_FOUND.getCode() + " | " + e.getMessage());
      return response;      
    }
  }
}
