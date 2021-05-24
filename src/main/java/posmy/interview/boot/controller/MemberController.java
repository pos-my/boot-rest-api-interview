/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.apiresponse.BaseApiResponse;
import posmy.interview.boot.model.request.BookReturnRequest;
import posmy.interview.boot.model.result.BaseResult;
import posmy.interview.boot.model.result.UserServiceResult;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

/**
 * @author Bennett
 * @version $Id: MemberController.java, v 0.1 2021-05-24 12:16 PM Bennett Exp $$
 */
@RestController
@RequestMapping("api/v1/member")
public class MemberController extends BaseController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @PostMapping("borrowBook/{bookTitle}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public BaseApiResponse borrowBook(@PathVariable String bookTitle) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        BaseResult result = bookService.borrowBook(bookTitle, currentPrincipalName);
        if(result.isSuccess()){
            return(BaseApiResponse)composeUserApiSuccessResponse("book borrow success", result);
        }
        return (BaseApiResponse)composeUserApiFailedResponse("book borrow failed", result);
    }

    @PostMapping("returnBook/{bookTitle}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public BaseApiResponse returnBook(@PathVariable String bookTitle) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        BaseResult result = bookService.returnBook(bookTitle, currentPrincipalName);
        if(result.isSuccess()){
            return(BaseApiResponse)composeUserApiSuccessResponse("book return success", result);
        }
        return (BaseApiResponse)composeUserApiFailedResponse("book return failed", result);
    }

    @GetMapping("viewBooks")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public BaseApiResponse getAllBooks() {
        BaseResult result = bookService.getAllBooks();
        return (BaseApiResponse)composeUserApiSuccessResponse("Query all books success", result);
    }

    @DeleteMapping("delete")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public BaseApiResponse removeOwnAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserServiceResult result = userService.deleteUserByUserName(currentPrincipalName);
        return (BaseApiResponse)composeUserApiSuccessResponse("user deleted", result);
    }

}