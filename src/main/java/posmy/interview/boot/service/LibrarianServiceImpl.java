package posmy.interview.boot.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
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
import posmy.interview.boot.enums.UserStatus;
import posmy.interview.boot.model.BaseResponse;
import posmy.interview.boot.model.librarian.AddBooksReq;
import posmy.interview.boot.model.librarian.AddMemberReq;
import posmy.interview.boot.model.librarian.BookBean;
import posmy.interview.boot.model.librarian.UsersBean;
import posmy.interview.boot.model.librarian.ViewMemberRes;

@Service
public class LibrarianServiceImpl implements LibrarianService {
  private static final Logger logger = LoggerFactory.getLogger(LibrarianServiceImpl.class);

  @Autowired
  UsersRepo usersRepo;

  @Autowired
  BookRepo bookRepo;

  @Override
  public BaseResponse addBooks(String authHeader, AddBooksReq request) {
    BaseResponse response = new BaseResponse();

    if (authHeader.isEmpty() || request.getBooks().isEmpty()) {
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

    } else if (user.getRole().equalsIgnoreCase(UserRole.LIBRARIAN.getCode()) == false) {
      logger.info("User ({}) does not have permission to add books", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }

    List<Book> toAdd = new ArrayList<Book>();
    for (BookBean book : request.getBooks()) {
      if (bookRepo.findByIsbn(book.getIsbn()) != null) {
        // Book with similar ISBN already exists
        logger.info("Book with ISBN({}) already exists, kindly remove book before trying again");
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setErrCode(ErrorCode.BOOK_ALREADY_EXISTS.getCode());
        response.setErrMsg(ErrorCode.BOOK_ALREADY_EXISTS.getDescription());
        return response;

      } else {
        // Add book into entity list
        Book newBook = new Book(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getYear(),
            BookStatus.AVAILABLE.getCode(), null);
        toAdd.add(newBook);
      }
    }

    try {
      logger.info("Adding new books...");
      bookRepo.saveAll(toAdd);
      logger.info("Added new books successfully");

      response.setStatus(ResponseStatus.SUCCESS.getCode());
      return response;
    } catch (Exception e) {
      logger.error("Exception found when adding new books", e);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.EXCEPTION_FOUND.getCode());
      response.setErrMsg(ErrorCode.EXCEPTION_FOUND.getCode() + " | " + e.getMessage());
      return response;
    }

  }

  @Override
  public BaseResponse updateBook(String authHeader, String isbn, String title, String author, String year,
      String status, String borrowedBy) {
    BaseResponse response = new BaseResponse();

    if (authHeader.isEmpty() || isbn.isEmpty()) {
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

    } else if (user.getRole().equalsIgnoreCase(UserRole.LIBRARIAN.getCode()) == false) {
      logger.info("User ({}) does not have permission to update book", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }

    Book updateBook = bookRepo.findByIsbn(isbn);
    if (updateBook == null) {
      logger.info("Book with isbn ({}) does not exist", isbn);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.BOOK_NOT_FOUND.getCode());
      response.setErrMsg(ErrorCode.BOOK_NOT_FOUND.getDescription());
      return response;
    }

    if (title.isEmpty() == false)
      updateBook.setTitle(title);
    if (author.isEmpty() == false)
      updateBook.setAuthor(author);
    if (year.isEmpty() == false)
      updateBook.setYear(year);
      
    if (status.isEmpty() == false){
      if(EnumUtils.isValidEnum(BookStatus.class, status.toUpperCase())) {
        updateBook.setStatus(status.toUpperCase());
      } else {
        logger.info("Unable to update book with ISBN({}) to status({})", isbn, status.toUpperCase());
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setErrCode(ErrorCode.INVALID_BOOK_STATUS.getCode());
        response.setErrMsg(ErrorCode.INVALID_BOOK_STATUS.getDescription());
        return response;
      }      
    }   
     
    if (borrowedBy.isEmpty() == false) {
      if(usersRepo.findByUsrIdIgnoreCaseAndRoleIgnoreCase(borrowedBy, UserRole.MEMBER.getCode()) != null) {
        updateBook.setBorrowedBy(borrowedBy);  
      } else {
        logger.info("Unable to update book with ISBN({}) to borrowedBy({})", isbn, borrowedBy);
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setErrCode(ErrorCode.INVALID_ACCOUNT.getCode());
        response.setErrMsg(ErrorCode.INVALID_ACCOUNT.getDescription());
        return response;
      }          
    }

    try {
      logger.info("Updating book with ISBN({})", isbn);
      bookRepo.save(updateBook);
      logger.info("Updated book successfully");

      response.setStatus(ResponseStatus.SUCCESS.getCode());
      return response;
    } catch (Exception e) {
      logger.error("Exception found when updating book", e);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.EXCEPTION_FOUND.getCode());
      response.setErrMsg(ErrorCode.EXCEPTION_FOUND.getCode() + " | " + e.getMessage());
      return response;
    }
  }

  @Override
  public BaseResponse removeBook(String authHeader, String isbn) {
    BaseResponse response = new BaseResponse();

    if (authHeader.isEmpty() || isbn.isEmpty()) {
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

    } else if (user.getRole().equalsIgnoreCase(UserRole.LIBRARIAN.getCode()) == false) {
      logger.info("User ({}) does not have permission to remove book", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }

    Book removeBook = bookRepo.findByIsbn(isbn);
    if (removeBook == null) {
      logger.info("Book with isbn ({}) does not exist", isbn);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.BOOK_NOT_FOUND.getCode());
      response.setErrMsg(ErrorCode.BOOK_NOT_FOUND.getDescription());
      return response;
    }

    // if book is borrowed/not available
    if(removeBook.getStatus() != BookStatus.AVAILABLE.getCode() || removeBook.getBorrowedBy() != null) {
      logger.info("Book with isbn ({}) is currently not available/borrowed by member ({}), kindly ask" 
          + " member to return the book before removing", isbn, removeBook.getBorrowedBy());
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.BOOK_NOT_FOUND.getCode());
      response.setErrMsg(ErrorCode.BOOK_NOT_FOUND.getDescription());
      return response;
    }
    
    try {
      logger.info("Removing book with ISBN({})", isbn);
      bookRepo.delete(removeBook);
      logger.info("Removed book successfully");

      response.setStatus(ResponseStatus.SUCCESS.getCode());
      return response;
    } catch (Exception e) {
      logger.error("Exception found when removing book", e);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.EXCEPTION_FOUND.getCode());
      response.setErrMsg(ErrorCode.EXCEPTION_FOUND.getCode() + " | " + e.getMessage());
      return response;
    }
  }

  @Override
  public BaseResponse addMember(String authHeader, AddMemberReq request) {
    BaseResponse response = new BaseResponse();

    if (authHeader.isEmpty() || request.getUsrId().isEmpty() || request.getRole().isEmpty()) {
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

    } else if (user.getRole().equalsIgnoreCase(UserRole.LIBRARIAN.getCode()) == false) {
      logger.info("User ({}) does not have permission to add member", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }

    if (usersRepo.findByUsrIdIgnoreCase(request.getUsrId()) != null) {
      logger.info("User ({}) already exists, unable to add user with same user id", request.getUsrId());
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_ALREADY_EXISTS.getCode());
      response.setErrMsg(ErrorCode.USER_ALREADY_EXISTS.getDescription());
      return response;
    }

    Users newUser = new Users(request.getUsrId(), request.getName(), request.getEmail(), UserRole.MEMBER.getCode(),
        UserStatus.ACTIVE.getCode());

    try {
      logger.info("Adding new member...");
      usersRepo.save(newUser);
      logger.info("Added new member successfully");

      response.setStatus(ResponseStatus.SUCCESS.getCode());
      return response;
    } catch (Exception e) {
      logger.error("Exception found when adding new member", e);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.EXCEPTION_FOUND.getCode());
      response.setErrMsg(ErrorCode.EXCEPTION_FOUND.getCode() + " | " + e.getMessage());
      return response;
    }
  }

  @Override
  public BaseResponse updateMember(String authHeader, String usrId, String name, String email, String role,
      String status) {
    BaseResponse response = new BaseResponse();

    if (authHeader.isEmpty() || usrId.isEmpty()) {
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

    } else if (user.getRole().equalsIgnoreCase(UserRole.LIBRARIAN.getCode()) == false) {
      logger.info("User ({}) does not have permission to update member", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }

    Users updateUser = usersRepo.findByUsrIdIgnoreCaseAndRoleIgnoreCase(usrId, UserRole.MEMBER.getCode());
    if (updateUser == null) {
      logger.info("Member ({}) not found", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.INVALID_ACCOUNT.getCode());
      response.setErrMsg(ErrorCode.INVALID_ACCOUNT.getDescription());
      return response;
    }

    if (name.isEmpty() == false)
      updateUser.setName(name);
    if (email.isEmpty() == false)
      updateUser.setEmail(email);

    try {
      logger.info("Updating member...");
      usersRepo.save(updateUser);
      logger.info("Updated new member successfully");

      response.setStatus(ResponseStatus.SUCCESS.getCode());
      return response;
    } catch (Exception e) {
      logger.error("Exception found when updating member ({})", usrId, e);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.EXCEPTION_FOUND.getCode());
      response.setErrMsg(ErrorCode.EXCEPTION_FOUND.getCode() + " | " + e.getMessage());
      return response;
    }
  }

  @Override
  public ViewMemberRes viewMember(String authHeader) {
    ViewMemberRes response = new ViewMemberRes();

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

    } else if (user.getRole().equalsIgnoreCase(UserRole.LIBRARIAN.getCode()) == false) {
      logger.info("User ({}) does not have permission to view member", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }
    logger.info("Retrieving members...");
    List<Users> memberList = usersRepo.findByRoleIgnoreCase(UserRole.MEMBER.getCode());
    
    List<UsersBean> resultList = new ArrayList<UsersBean>();
    for(Users member: memberList) {
      UsersBean memberObj = new UsersBean(member.getUsrId(), member.getName(), member.getEmail(), member.getRole(), member.getStatus());
      resultList.add(memberObj);
    }
    logger.info("Retrieved members successfully");
    response.setStatus(ResponseStatus.SUCCESS.getCode());
    response.setMemberList(resultList);
    return response;
  }

  @Override
  public BaseResponse removeMember(String authHeader, String usrId) {
    BaseResponse response = new BaseResponse();

    if (authHeader.isEmpty() || usrId.isEmpty()) {
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

    } else if (user.getRole().equalsIgnoreCase(UserRole.LIBRARIAN.getCode()) == false) {
      logger.info("User ({}) does not have permission to remove member", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.USER_UNAUTHORIZED.getCode());
      response.setErrMsg(ErrorCode.USER_UNAUTHORIZED.getDescription());
      return response;
    }

    Users removeUser = usersRepo.findByUsrIdIgnoreCaseAndRoleIgnoreCase(usrId, UserRole.MEMBER.getCode());
    if (removeUser == null) {
      logger.info("Member ({}) not found", authHeader);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.INVALID_ACCOUNT.getCode());
      response.setErrMsg(ErrorCode.INVALID_ACCOUNT.getDescription());
      return response;
    }
    
    List<Book> bookList = bookRepo.findByBorrowedByIgnoreCase(usrId);
    if(bookList.size() > 1) {
      logger.info("Member ({}) still has {} borrowed books, kindly ask member to return books before removing", authHeader, bookList.size());
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.PENDING_RETURN_BOOKS.getCode());
      response.setErrMsg(ErrorCode.PENDING_RETURN_BOOKS.getDescription());
      return response;
    }    
    
    try {
      logger.info("Removing member with usrId({})", usrId);
      usersRepo.delete(removeUser);
      logger.info("Removed member successfully");

      response.setStatus(ResponseStatus.SUCCESS.getCode());
      return response;
    } catch (Exception e) {
      logger.error("Exception found when removing member", e);
      response.setStatus(ResponseStatus.FAILED.getCode());
      response.setErrCode(ErrorCode.EXCEPTION_FOUND.getCode());
      response.setErrMsg(ErrorCode.EXCEPTION_FOUND.getCode() + " | " + e.getMessage());
      return response;
    }
  }
}
