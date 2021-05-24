/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.apiresponse.BaseApiResponse;
import posmy.interview.boot.model.request.UserRequest;
import posmy.interview.boot.model.result.BaseResult;
import posmy.interview.boot.model.result.UserQueryResult;
import posmy.interview.boot.model.result.UserServiceResult;
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
    public BaseApiResponse createUser(@RequestBody UserRequest request) {
        BaseResult result = userService.createUser(request);
        if(result.isSuccess()){
            return (BaseApiResponse) composeApiSuccessResponse("User created", result);
        }
        return (BaseApiResponse) composeApiFailedResponse("User creation failed", result);
    }

    @PostMapping("update")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public BaseApiResponse updateUser(@RequestBody UserRequest request) {
        BaseResult result = userService.updateUser(request);
        if(result.isSuccess()){
            return(BaseApiResponse) composeApiSuccessResponse("User update success", result);
        }
        return(BaseApiResponse) composeApiFailedResponse("User update failed", result);
    }

    @GetMapping("view/{username}")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public BaseApiResponse getUserByUsername(@PathVariable String username) {
        UserQueryResult result = userService.getUserByUserName(username);
        if(result.isSuccess()){
            return(BaseApiResponse) composeApiSuccessResponse("User query success", result);
        }
        return (BaseApiResponse) composeApiFailedResponse("User query failed", result);
    }

    @GetMapping("view/all")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public BaseApiResponse getAllUsers() {
        UserQueryResult result = userService.getAllUsers();
        return (BaseApiResponse) composeApiSuccessResponse("Query all users success", result);
    }

    @DeleteMapping("delete/{username}")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    public BaseApiResponse removeUser(@PathVariable String username) {
        UserServiceResult result = userService.deleteUserByUserName(username);
        return (BaseApiResponse) composeApiSuccessResponse("user deleted", result);
    }
}