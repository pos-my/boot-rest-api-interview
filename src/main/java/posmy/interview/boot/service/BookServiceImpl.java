package posmy.interview.boot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import posmy.interview.boot.constant.Constants;
import posmy.interview.boot.database.BookDao;
import posmy.interview.boot.database.TransactionDao;
import posmy.interview.boot.database.UserDao;
import posmy.interview.boot.exception.BookNotAvailableException;
import posmy.interview.boot.exception.InvalidArgumentException;
import posmy.interview.boot.exception.UnauthorisedException;
import posmy.interview.boot.model.book.Book;
import posmy.interview.boot.model.book.BookCreatedResponse;
import posmy.interview.boot.model.book.BookResponse;
import posmy.interview.boot.model.book.UpdateBookResponse;
import posmy.interview.boot.model.common.Pagination;
import posmy.interview.boot.model.database.BookEntity;
import posmy.interview.boot.model.database.TransactionEntity;
import posmy.interview.boot.model.security.CustomUserDetail;
import posmy.interview.boot.util.DateUtil;
import posmy.interview.boot.util.Json;
import posmy.interview.boot.util.PaginationUtil;
import posmy.interview.boot.util.ValidationUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {

    BookDao bookDao;
    TransactionDao transactionDao;
    UserDao userDao;

    ObjectMapper objectMapper = new ObjectMapper();

    public BookServiceImpl(BookDao bookDao, TransactionDao transactionDao, UserDao userDao){
        this.bookDao = bookDao;
        this.transactionDao = transactionDao;
        this.userDao = userDao;
    }


    @Override
    public BookResponse processBookQueryRequest(int pageSize, int pageNumber, String name, String description, String status) {
        pageNumber = pageNumber - 1;
        BookResponse bookResponse = new BookResponse();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        name = reformatQueryString(name);
        description = reformatQueryString(description);

        Page<BookEntity> bookEntities = bookDao.getBooksByNameAndDescriptionAndStatus(name,
                description, status, pageable);

        List<Book> books = new ArrayList<>();
        for (BookEntity bookEntity: bookEntities){
           Book book = new Book();
           book.setBookId(bookEntity.getBookId());
           book.setName(bookEntity.getName());
           book.setStatus(bookEntity.getStatus());
           book.setDescription(bookEntity.getDescription());
           book.setRecordCreateDate(DateUtil.convertToStandardDateStringFormat(bookEntity.getRecordCreateDate().getTime()));
           book.setRecordUpdateDate(DateUtil.convertToStandardDateStringFormat(bookEntity.getRecordUpdateDate().getTime()));
           books.add(book);
        }

        bookResponse.setRecords(books);

        Pagination pagination = PaginationUtil.createPagination((int)bookEntities.getTotalElements(), pageSize, pageNumber);
        bookResponse.setPagination(pagination);

        return bookResponse;
    }

    @Override
    public BookCreatedResponse createBook(String name, String description, String status){
        BookEntity createBookEntity = new BookEntity();
        Calendar currentDateTime = Calendar.getInstance();
        createBookEntity.setName(name);
        createBookEntity.setDescription(description);
        createBookEntity.setStatus(status);
        createBookEntity.setRecordCreateDate(new Timestamp(currentDateTime.getTime().getTime()));
        createBookEntity.setRecordUpdateDate(new Timestamp(currentDateTime.getTime().getTime()));

        BookEntity bookEntity = bookDao.save(createBookEntity);
        BookCreatedResponse bookCreatedResponse = new BookCreatedResponse();
        bookCreatedResponse.setBookId(bookEntity.getBookId());
        bookCreatedResponse.setName(bookEntity.getName());
        bookCreatedResponse.setDescription(bookEntity.getDescription());
        bookCreatedResponse.setStatus(bookEntity.getStatus());
        bookCreatedResponse.setRecordCreateDate(DateUtil.convertToStandardDateStringFormat(bookEntity.getRecordCreateDate().getTime()));
        bookCreatedResponse.setRecordUpdateDate(DateUtil.convertToStandardDateStringFormat(bookEntity.getRecordUpdateDate().getTime()));
        return bookCreatedResponse;
    }

    @Override
    public UpdateBookResponse updateBook(Integer id, String name, String description, String status) throws InvalidArgumentException, UnauthorisedException{
        BookEntity existingBookEntity = bookDao.findByBookId(id);
        if (existingBookEntity == null) {
            throw new InvalidArgumentException();
        }

        String bookOriginalStatus = existingBookEntity.getStatus();
        String role = getCurrentUserRole();
        checkIfUserHasAuthority(getCurrentUserRole(), status);

        //only librarian can update this
        if (role.equals(Constants.ROLE_LIBRARIAN)) {
            if (!ValidationUtil.isStringEmpty(name)) {
                existingBookEntity.setName(name);
            }

            if (!ValidationUtil.isStringEmpty(description)) {
                existingBookEntity.setDescription(description);
            }
        } else {
            checkIfBookAlreadyBorrowed(existingBookEntity, status);
        }

        if (!ValidationUtil.isStringEmpty(status)){
            existingBookEntity.setStatus(status);
        }

        UpdateBookResponse updateBookResponse = updateBookInDb(existingBookEntity);

        if (status != null && status.length() > 0 && !bookOriginalStatus.equals(status)) {
            updateTransactionInDb(existingBookEntity, status);
        }

        return updateBookResponse;
    }

    private void checkIfBookAlreadyBorrowed(BookEntity bookEntity, String status) throws BookNotAvailableException{
        if (!status.equals(Constants.BookStatus.BORROWED.getType())) return;

        if (!bookEntity.getStatus().equals(Constants.BookStatus.AVAILABLE.getType())){
            throw new BookNotAvailableException();
        }
    }

    private void checkIfUserHasAuthority(String role, String status) throws UnauthorisedException{
        if (role.equals(Constants.ROLE_LIBRARIAN) ){
            return;
        } else if (role.equals(Constants.ROLE_MEMBER) &&
                (status.equals(Constants.BookStatus.AVAILABLE.getType()) || status.equals(Constants.BookStatus.BORROWED.getType()))){
            return;
        }
        throw new UnauthorisedException();
    }

    private UpdateBookResponse updateBookInDb(BookEntity existingBookEntity) {
        Calendar currentDateTime = Calendar.getInstance();
        existingBookEntity.setRecordUpdateDate(new Timestamp(currentDateTime.getTime().getTime()));
        BookEntity result = bookDao.save(existingBookEntity);
        UpdateBookResponse updateBookResponse = new UpdateBookResponse();
        updateBookResponse.setBookId(result.getBookId());
        updateBookResponse.setName(result.getName());
        updateBookResponse.setDescription(result.getDescription());
        updateBookResponse.setStatus(result.getStatus());
        updateBookResponse.setRecordCreateDate(DateUtil.convertToStandardDateStringFormat(result.getRecordCreateDate().getTime()));
        updateBookResponse.setRecordUpdateDate(DateUtil.convertToStandardDateStringFormat(result.getRecordUpdateDate().getTime()));
        return updateBookResponse;
    }

    private void updateTransactionInDb(BookEntity bookEntity, String bookStatus) {
        Calendar currentDateTime = Calendar.getInstance();
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setBookEntity(bookEntity);
        transactionEntity.setUserEntity(userDao.findUserEntityByUserName(getCurrentUsername()));
        transactionEntity.setStatus(mapBookStatusToTransactionStatus(bookStatus));
        transactionEntity.setRecordCreateDate(new Timestamp(currentDateTime.getTime().getTime()));
        transactionEntity.setRecordUpdateDate(new Timestamp(currentDateTime.getTime().getTime()));
        transactionDao.save(transactionEntity);
    }

    private String mapBookStatusToTransactionStatus(String bookStatus){
        if (bookStatus.equals(Constants.BookStatus.AVAILABLE.getType())){
            return Constants.TransactionStatus.RETURNED.getType();
        } else if (bookStatus.equals(Constants.BookStatus.BORROWED.getType())){
            return Constants.TransactionStatus.BORROWED.getType();
        } else {
            return Constants.TransactionStatus.REMOVED.getType();
        }
    }

    private void insertTransaction(BookEntity existingBookEntity) {
        TransactionEntity transactionEntity = new TransactionEntity();
        Calendar currentDateTime = Calendar.getInstance();
        transactionEntity.setRecordCreateDate(new Timestamp(currentDateTime.getTime().getTime()));
        transactionEntity.setRecordUpdateDate(new Timestamp(currentDateTime.getTime().getTime()));
    }

    private String reformatQueryString(String input){
        if (input==null || input.replace(" ", "").length() == 0){
            return null;
        } else {
            return "%" + input + "%";
        }
    }

    private String getCurrentUserRole(){
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = "";
        for (GrantedAuthority authority : userDetail.getAuthorities()) {
            role = authority.getAuthority();
            break;
        }
        return role;
    }

    private String getCurrentUsername(){
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetail.getUsername();
    }
}
