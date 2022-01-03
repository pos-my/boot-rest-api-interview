package posmy.interview.boot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import posmy.interview.boot.database.BookDao;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.BookResponse;
import posmy.interview.boot.model.common.Pagination;
import posmy.interview.boot.model.database.BookEntity;
import posmy.interview.boot.util.DateUtil;
import posmy.interview.boot.util.PaginationUtil;

import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements BookService {

    BookDao bookDao;

    public BookServiceImpl(BookDao bookDao){
        this.bookDao = bookDao;
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

    private String reformatQueryString(String input){
        if (input==null || input.replace(" ", "").length() == 0){
            return null;
        } else {
            return "%" + input + "%";
        }
    }
}
