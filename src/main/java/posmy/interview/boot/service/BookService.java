/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.service;

import posmy.interview.boot.model.request.BookRequest;
import posmy.interview.boot.model.result.BookQueryServiceResult;
import posmy.interview.boot.model.result.BookServiceResult;

/**
 * @author Bennett
 * @version $Id: BookServiceImpl.java, v 0.1 2021-05-24 11:31 PM Bennett.hds Exp $$
 */
public interface BookService {
    BookServiceResult addBook(BookRequest request);

    BookServiceResult updateBook(BookRequest request);

    BookServiceResult deleteBookByBookTitle(String bookTitle);

    BookServiceResult borrowBook(String bookTitle, String username);

    BookServiceResult returnBook(String bookTitle, String username);

    BookQueryServiceResult getAllBooks();
}