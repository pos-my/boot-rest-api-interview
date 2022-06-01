package posmy.interview.boot.service.api;

import posmy.interview.boot.model.Book;

import java.util.List;

public interface BookService {
    /**
     * save all books
     *
     * @param books data
     * @return saved data
     */
    List<Book> saveAll(List<Book> books);

    /**
     * borrow book that are available, for future proof, using list
     *
     * @param ids borrowing book's ids
     * @return borrowed book info
     */
    List<Book> borrow(List<Long> ids);

    /**
     * return book that are borrowed, for future proof, using list
     *
     * @param ids return book's ids
     * @return return book info
     */
    List<Book> returnBook(List<Long> ids);

    /**
     * get all the books by name, it is list because the book can be same name (translated by and etc)
     *
     * @param name book name
     * @return list of book
     */
    List<Book> findByName(String name);

}
