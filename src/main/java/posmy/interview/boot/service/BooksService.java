package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.Books;
import posmy.interview.boot.repository.BooksRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BooksService {

    @Autowired
    BooksRepository booksRepository;

    //getting all books record by using the method findaAll()
    public List<Books> getAllBooks()
    {
        List<Books> books = new ArrayList<Books>();
        booksRepository.findAll().forEach(books1 -> books.add(books1));
        return books;
    }

    //getting a specific record by using the method findById()
    public Books getBooksById(int id)
    {
        return booksRepository.findById(id).get();
    }

    //saving a specific record by using the method save()
    public void saveOrUpdate(Books books)
    {
        booksRepository.save(books);
    }

    //deleting a specific record by using the method deleteById()
    public void delete(int id)
    {
        booksRepository.deleteById(id);
    }

    //updating a record
    public void update(Books books, int bookid)
    {
        booksRepository.save(books);
    }

    public void returnBook(Long bookid)
    {
        booksRepository.returnBook(bookid);
    }

    public void borrowBooks(Long bookid)
    {
        booksRepository.borrowBooks(bookid);
    }

}
