package posmy.interview.boot.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.exception.BadRequestException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRepo;
import posmy.interview.boot.service.api.BookService;
import posmy.interview.boot.service.api.UserService;
import posmy.interview.boot.snowflake.SnowflakeHelper;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private UserService userService;

    @Override
    public List<Book> saveAll(List<Book> books) {
        log.info("save books: {}", books);
        //set id when it is new role insert
        SnowflakeHelper.assignLongIds(books);
        //save and return the saved data
        return bookRepo.saveAll(books);
    }

    @Override
    public List<Book> borrow(List<Long> ids) {
        log.info("borrow books by ids: {}", ids);
        //get all books by ids
        var dbBooks = bookRepo.findAllById(ids);
        //get user
        var user = userService.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        for (Book dbBook : dbBooks) {
            //validate the book is not yet borrow
            if (dbBook.getBorrowBy() != null && !Objects.equals(dbBook.getBorrowBy(), user.getId())) {
                throw new BadRequestException("Book is borrowed by others.");
            }
            //update the borrow id = me
            dbBook.setBorrowBy(user.getId());
        }

        //save
        return this.saveAll(dbBooks);
    }

    @Override
    public List<Book> returnBook(List<Long> ids) {
        log.info("return books by ids: {}", ids);
        //get all books by ids
        var dbBooks = bookRepo.findAllById(ids);
        for (Book dbBook : dbBooks) {
            dbBook.setBorrowBy(null);
        }
        //save
        return this.saveAll(dbBooks);
    }

    @Override
    public List<Book> findByName(String name) {
        log.info("find by name: {}", name);
        return this.bookRepo.findByName(name);
    }


}
