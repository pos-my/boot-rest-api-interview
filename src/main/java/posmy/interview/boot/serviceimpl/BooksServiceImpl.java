/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.serviceimpl;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.dao.BooksDao;
import posmy.interview.boot.entity.Books;
import posmy.interview.boot.entity.Librarian;
import posmy.interview.boot.entity.Members;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.exception.AppException;
import posmy.interview.boot.service.BooksService;
import posmy.interview.boot.service.LibrarianService;
import posmy.interview.boot.service.MembersService;
import posmy.interview.boot.util.Constants;

/**
 *
 * @author syahirghariff
 */
@Service
public class BooksServiceImpl implements BooksService {

    @Autowired
    private BooksDao booksDao;

    @Autowired
    private LibrarianService librarianSvc;
    
    @Autowired 
    private MembersService membersSvc;

    @Override
    public List<Books> getAllBooks() {
        return booksDao.getAllBooks();
    }

    @Override
    @Transactional
    public Books saveOrUpdate(Books req) {

        // Find Librarian 
        Librarian librarian = librarianSvc.findFromLogin();

        return booksDao.saveOrUpdate(new Books(req, librarian));
    }

    @Override
    @Transactional
    public Books borrowBooks(String bookName) {
        
        List<Books> books = booksDao.findAllByBookName(bookName);
        
        Books res = null;
       
        boolean borrowed = false;
        for (Books book : books) {
            if (book.getStatus().equals(BookStatus.AVAILABLE) && !borrowed) {
                
                Members members = membersSvc.findFromLogin();
                
                borrowed = true;
                
                book.setStatus(BookStatus.BORROWED);
                book.setBorrowedBy(members.getUserId());
                res = booksDao.saveOrUpdate(book);
                
            }
        }
        
        if (res == null) {
            throw new AppException(Constants.NO_BOOKS_AVAILABLE);
        }
        
        return res;
    }

    @Override
    @Transactional
    public Books returnBooks(String bookName) {
        
        Members members = membersSvc.findFromLogin();
        
        Books books = booksDao.findBookByBookNameAndMembers(bookName, members.getId()); 
        
        books.setStatus(BookStatus.AVAILABLE);
        books.setBorrowedBy(null);
        
        return booksDao.saveOrUpdate(books);
    }

    @Override
    @Transactional
    public void delete(String id) {

        // Check if books exist in DB 
        Books books = booksDao.findById(id);
        if (books == null) {
            throw new AppException(Constants.ID_NOT_FOUND.concat(id));
        }

        booksDao.deleteById(id);
    }

}
