/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.apiresponse.BookApiResponse;
import posmy.interview.boot.model.entity.Book;
import posmy.interview.boot.service.BookService;

/**
 * @author Bennett
 * @version $Id: MemberController.java, v 0.1 2021-05-24 12:16 PM Bennett Exp $$
 */
@RestController
@RequestMapping("api/v1/member")
public class MemberController {
    @Autowired
    private BookService bookService;

    @GetMapping("viewbooks")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public BookApiResponse viewBooks() {
        return null;
    }

    @PostMapping("borrow")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public BookApiResponse borrowBook(@RequestBody Book book) {
        return null;
    }

    @GetMapping("return")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public BookApiResponse returnBook(@RequestBody Book book) {
        return null;
    }

}