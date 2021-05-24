/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.apiresponse.UserApiResponse;
import posmy.interview.boot.model.entity.User;
import posmy.interview.boot.model.request.CreateUserRequest;
import posmy.interview.boot.model.result.BaseResult;
import posmy.interview.boot.service.UserService;

/**
 * @author Bennett
 * @version $Id: LibrarianController.java, v 0.1 2021-05-24 12:16 PM Bennett Exp $$
 */
@RestController
@RequestMapping("api/v1/librarian")
public class LibrarianController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping("create")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public UserApiResponse createUser(@RequestBody CreateUserRequest request) {
        BaseResult result = userService.createUser(request);
        if(result.isSuccess()){
            return composeUserApiSuccessResponse(request.getUsername(), "User created");
        }
        return composeUserApiFailedResponse(request.getUsername(),"User creation failed", result);
    }

    @PostMapping("update")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public UserApiResponse updateUser(@RequestBody User user) {
        return null;
    }

    @GetMapping("view/{username}")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public UserApiResponse getUserByUsername(@PathVariable String username) {
        return null;
    }

    @DeleteMapping("delete/{username}")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public UserApiResponse removeUser(@PathVariable String username) {
        return null;
    }
}