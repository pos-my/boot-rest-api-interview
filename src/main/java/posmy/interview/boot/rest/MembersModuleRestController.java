/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.entity.Members;
import posmy.interview.boot.service.BooksService;
import posmy.interview.boot.service.MembersService;
import posmy.interview.boot.util.Constants;
import posmy.interview.boot.util.ResponseUtil;

/**
 *
 * @author syahirghariff
 */
@RestController
@RequestMapping("/members")
public class MembersModuleRestController {
    
    @Autowired
    private BooksService booksSvc;
    
    @Autowired
    private MembersService membersSvc; 
    
    
      // BOOKS //
    
    @GetMapping("/get_all_books")
    public ResponseEntity getAllBooks() {
        try {
            booksSvc.getAllBooks();
            
            return ResponseUtil.success(booksSvc.getAllBooks());
        } catch (Exception error) {
            return ResponseUtil.exception(error);
        }
    }
    
    
    @PostMapping("borrow_books")
    public ResponseEntity borrowBooks(@RequestBody String bookName) {
        try {
            return ResponseUtil.success(booksSvc.borrowBooks(bookName));
        } catch (Exception error) {
            return ResponseUtil.exception(error);
        }
    }
    
    @PostMapping("return_books")
    public ResponseEntity returnBooks(@RequestBody String bookName) {
        try {
            return ResponseUtil.success(booksSvc.borrowBooks(bookName));
        } catch (Exception error) {
            return ResponseUtil.exception(error);
        }
    }
    
    // MEMBERS //  
    
    @PostMapping("delete_account")
    public ResponseEntity deleteAccount() {
        try {
            Members members = membersSvc.findFromLogin();
            membersSvc.delete(members.getId());
            return ResponseUtil.success(Constants.SUCCESS_MSG_DELETED);
        } catch (Exception error) {
            return ResponseUtil.exception(error);
        }
    }
    
}
