/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.entity.Book;
import posmy.interview.boot.model.entity.User;
import posmy.interview.boot.model.enums.BookStatusEnum;
import posmy.interview.boot.model.enums.ErrorCodeEnum;
import posmy.interview.boot.model.request.BookRequest;
import posmy.interview.boot.model.result.BookQueryServiceResult;
import posmy.interview.boot.model.result.BookServiceResult;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.UserRespository;
import posmy.interview.boot.service.BookService;

import java.util.List;
import java.util.Optional;

/**
 * @author Bennett
 * @version $Id: BookServiceImpl.java, v 0.1 2021-05-24 12:20 PM Bennett Exp $$
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRespository userRespository;

    @Override
    public BookServiceResult addBook(BookRequest request){
        BookServiceResult result = new BookServiceResult();
        Optional<Book> book = bookRepository.findByBookTitle(request.getBookTitle());
        if(book.isPresent()){
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.BOOK_EXIST.getCode());
            result.setErrorDesc(ErrorCodeEnum.BOOK_EXIST.getDescription());
            result.setBookTitle(request.getBookTitle());
        }
        else {
            Book newBook = new Book();
            newBook.setBookTitle(request.getBookTitle());
            newBook.setAuthor(request.getAuthor());
            newBook.setBookStatus(BookStatusEnum.AVAILABLE.getCode());
            bookRepository.save(newBook);

            result.setSuccess(true);
            result.setBookTitle(request.getBookTitle());
        }
        return result;
    }

    @Override
    public BookServiceResult updateBook(BookRequest request){
        BookServiceResult result = new BookServiceResult();
        Optional<Book> book = bookRepository.findByBookTitle(request.getBookTitle());
        if(!book.isPresent()){
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.BOOK_NOT_FOUND.getCode());
            result.setErrorDesc(ErrorCodeEnum.BOOK_NOT_FOUND.getDescription());
            result.setBookTitle(request.getBookTitle());
        }
        else {
            Book updateBook = book.get();
            updateBook.setBookTitle(request.getBookTitle());
            updateBook.setAuthor(request.getAuthor());
            updateBook.setBookStatus(request.getStatus());
            updateBook.setUserBorrow(request.getUserBorrow());
            bookRepository.save(updateBook);

            result.setSuccess(true);
            result.setBookTitle(request.getBookTitle());
            result.setBorrowedUser(request.getUserBorrow());
        }
        return result;
    }

    @Override
    public BookServiceResult deleteBookByBookTitle(String bookTitle){
        BookServiceResult result = new BookServiceResult();
        Optional<Book> book = bookRepository.findByBookTitle(bookTitle);
        if(book.isPresent()){
            bookRepository.delete(book.get());
        }
        result.setSuccess(true);
        result.setBookTitle(bookTitle);
        return result;
    }

    @Override
    public BookServiceResult borrowBook(String bookTitle, String username){
        BookServiceResult result = new BookServiceResult();
        Optional<Book> book = bookRepository.findByBookTitle(bookTitle);
        if(!book.isPresent()){
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.BOOK_NOT_FOUND.getCode());
            result.setErrorDesc(ErrorCodeEnum.BOOK_NOT_FOUND.getDescription());
            result.setBookTitle(bookTitle);
            return result;
        }

        if(book.get().getBookStatus().equals(BookStatusEnum.BORROWED.getCode())){
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.BOOK_NOT_AVAILABLE.getCode());
            result.setErrorDesc(ErrorCodeEnum.BOOK_NOT_AVAILABLE.getDescription());
            result.setBookTitle(bookTitle);
            return result;
        }

        Optional<User> user = userRespository.findByUsername(username);
        if(!user.isPresent()){
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.USER_NOT_FOUND.getCode());
            result.setErrorDesc(ErrorCodeEnum.USER_NOT_FOUND.getDescription());
            return result;
        }

        Book borrowBook = book.get();
        borrowBook.setUserBorrow(user.get().getUsername());
        borrowBook.setBookStatus(BookStatusEnum.BORROWED.getCode());

        bookRepository.save(borrowBook);

        result.setSuccess(true);
        result.setBookTitle(bookTitle);
        result.setBorrowedUser(username);
        return result;
    }

    @Override
    public BookServiceResult returnBook(String bookTitle, String username){
        BookServiceResult result = new BookServiceResult();
        Optional<Book> book = bookRepository.findByBookTitle(bookTitle);
        if(!book.isPresent()){
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.BOOK_NOT_FOUND.getCode());
            result.setErrorDesc(ErrorCodeEnum.BOOK_NOT_FOUND.getDescription());
            result.setBookTitle(bookTitle);
            result.setBorrowedUser(username);
            return result;
        }

        if(book.get().getBookStatus().equals(BookStatusEnum.AVAILABLE.getCode())){
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.BOOK_ALREADY_RETURN.getCode());
            result.setErrorDesc(ErrorCodeEnum.BOOK_ALREADY_RETURN.getDescription());
            result.setBookTitle(bookTitle);
            return result;
        }

        Book returnBook = book.get();
        returnBook.setBookStatus(BookStatusEnum.AVAILABLE.getCode());
        returnBook.setUserBorrow("NONE");

        bookRepository.save(returnBook);

        result.setSuccess(true);
        result.setBookTitle(bookTitle);
        return result;
    }

    @Override
    public BookQueryServiceResult getAllBooks() {
        BookQueryServiceResult result = new BookQueryServiceResult();
        List<Book> bookList = bookRepository.findBookByStatus(BookStatusEnum.AVAILABLE.getCode());
        result.setSuccess(true);
        result.setBookList(bookList);
        return result;
    }
}