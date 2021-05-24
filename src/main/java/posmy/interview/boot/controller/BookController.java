/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.apiresponse.BaseApiResponse;
import posmy.interview.boot.model.request.BookRequest;
import posmy.interview.boot.model.result.BaseResult;
import posmy.interview.boot.service.BookService;

/**
 * @author Bennett
 * @version $Id: BookController.java, v 0.1 2021-05-24 7:36 PM Bennett Exp $$
 */
@RestController
@RequestMapping("api/v1/book")
public class BookController extends BaseController{
    @Autowired
    private BookService bookService;

    @PostMapping("add")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public BaseApiResponse addBook(@RequestBody BookRequest request) {
        BaseResult result = bookService.addBook(request);
        return (BaseApiResponse) composeApiSuccessResponse("Book added successfully", result);
    }

    @PostMapping("update")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public BaseApiResponse updateBook(@RequestBody BookRequest request) {
        BaseResult result = bookService.updateBook(request);
        if(result.isSuccess()){
            return(BaseApiResponse) composeApiSuccessResponse("book updated success", result);
        }
        return (BaseApiResponse) composeApiFailedResponse("book update failed", result);
    }

    @DeleteMapping("delete/{bookTitle}")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public BaseApiResponse deleteBook(@PathVariable String bookTitle) {
        BaseResult result = bookService.deleteBookByBookTitle(bookTitle);
        return (BaseApiResponse) composeApiSuccessResponse("book deleted", result);
    }
}