package posmy.interview.boot.services.book;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.db.BookDal;
import posmy.interview.boot.helper.DateHelper;
import posmy.interview.boot.model.entity.BookEntity;
import posmy.interview.boot.model.request.BookRequest;
import posmy.interview.boot.model.response.BookResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDal bookDal;

    @Override
    public void remove(String id) {
        Optional<BookEntity> bookEntity = this.bookDal.findById(id);

        if(bookEntity.isEmpty()){
            throw new ObjectNotFoundException("Book with ID = {} cannot be found", id);
        }

        this.bookDal.deleteById(id);
    }

    @Override
    public List<BookResponse> viewAll() {
        List<BookResponse> bookResponseList = new ArrayList<>();
        this.bookDal.findAll().forEach(val ->
               bookResponseList.add(
                       new BookResponse(
                               val.getBookId(),
                               val.getBookTitle(),
                               val.getGenre(),
                               val.getStatus(),
                               DateHelper.displayDate(val.getDateCreated()),
                               DateHelper.displayDate(val.getDateUpdated())
                       )
               )
        );
        return bookResponseList;
    }

    @Override
    public BookResponse view(String id) {
        Optional<BookEntity> bookEntity = this.bookDal.findById(id);

        if(bookEntity.isEmpty()){
            throw new ObjectNotFoundException("Book with ID = {} cannot be found", id);
        }

        BookEntity book = bookEntity.get();

        //Return
        return new BookResponse(
                book.getBookId(),
                book.getBookTitle(),
                book.getGenre(),
                book.getStatus(),
                DateHelper.displayDate(book.getDateCreated()),
                DateHelper.displayDate(book.getDateUpdated())
        );
    }

    @Override
    public BookResponse save(BookRequest bookRequest) {
        //Save
        BookEntity book = new BookEntity();
        book.setBookTitle(bookRequest.getTitle());
        book.setGenre(bookRequest.getGenre());
        book.setStatus("AVAILABLE");
        book.setDateCreated(new Date());
        book.setDateUpdated(new Date());
        this.bookDal.save(book);

        //Return
        return new BookResponse(
                book.getBookId(),
                book.getBookTitle(),
                book.getGenre(),
                book.getStatus(),
                DateHelper.displayDate(book.getDateCreated()),
                DateHelper.displayDate(book.getDateUpdated())
        );
    }

    @Override
    public BookResponse update(BookRequest bookRequest) {
        Optional<BookEntity> bookEntity = this.bookDal.findById(bookRequest.getId());

        if(bookEntity.isEmpty()){
            throw new ObjectNotFoundException("Book with ID = {} cannot be found", bookRequest.getId());
        }

        //Save
        BookEntity book = bookEntity.get();
        book.setBookTitle(bookRequest.getTitle());
        book.setGenre(bookRequest.getGenre());
        if(bookRequest.getStatus() != null && !bookRequest.getStatus().isEmpty()) {
            book.setStatus(bookRequest.getStatus());
        }
        book.setDateUpdated(new Date());
        this.bookDal.save(book);

        //Return
        return new BookResponse(
                book.getBookId(),
                book.getBookTitle(),
                book.getGenre(),
                book.getStatus(),
                DateHelper.displayDate(book.getDateCreated()),
                DateHelper.displayDate(book.getDateUpdated())
        );
    }

    @Override
    public BookResponse borrow(String id) {
        Optional<BookEntity> bookEntity = this.bookDal.findById(id);

        if(bookEntity.isEmpty()){
            throw new ObjectNotFoundException("Book with ID = {} cannot be found", id);
        }

        //Save
        BookEntity book = bookEntity.get();
        book.setStatus("BORROWED");
        book.setDateUpdated(new Date());
        this.bookDal.save(book);

        //Return
        return new BookResponse(
                book.getBookId(),
                book.getBookTitle(),
                book.getGenre(),
                book.getStatus(),
                DateHelper.displayDate(book.getDateCreated()),
                DateHelper.displayDate(book.getDateUpdated())
        );
    }

    @Override
    public BookResponse returnBook(String id) {
        Optional<BookEntity> bookEntity = this.bookDal.findById(id);

        if(bookEntity.isEmpty()){
            throw new ObjectNotFoundException("Book with ID = {} cannot be found", id);
        }

        //Save
        BookEntity book = bookEntity.get();
        book.setStatus("AVAILABLE");
        book.setDateUpdated(new Date());
        this.bookDal.save(book);

        //Return
        return new BookResponse(
                book.getBookId(),
                book.getBookTitle(),
                book.getGenre(),
                book.getStatus(),
                DateHelper.displayDate(book.getDateCreated()),
                DateHelper.displayDate(book.getDateUpdated())
        );
    }
}
