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
import posmy.interview.boot.entity.Books;
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
@RequestMapping("/librarian")
public class LibrarianModuleRestController {

    @Autowired
    private BooksService booksSvc;

    @Autowired
    private MembersService membersSvc;
    
    // BOOKS //
    
    @PostMapping("save_books")
    public ResponseEntity saveOrUpdateBooks (@RequestBody Books books) {
        try {
            return ResponseUtil.success(booksSvc.saveOrUpdate(books));
        } catch (Exception error) {
            return ResponseUtil.exception(error);
        }
    }
    
    
    @PostMapping("delete_books")
    public ResponseEntity deleteBooks(@RequestBody String id) {
        try {
            booksSvc.delete(id);
            return ResponseUtil.success(Constants.SUCCESS_MSG_DELETED);
        } catch (Exception error) {
            return ResponseUtil.exception(error);
        }
    }
    
    
    
    
    // MEMBERS //
    
    @GetMapping("/get_all_members")
    public ResponseEntity getAllMembers() {
        try {
            return ResponseUtil.success(membersSvc.getAllMembers());
        } catch (Exception error) {
            return ResponseUtil.exception(error);
        }
    }
    
    
    @PostMapping("/save_members")
    public ResponseEntity saveOrUpdateMembers(@RequestBody Members members) {
        try {
            return ResponseUtil.success(membersSvc.saveOrUpdate(members));
        } catch (Exception error) {
            return ResponseUtil.exception(error);
        }
    }
    
    @PostMapping("/delete_members")
    public ResponseEntity deleteMembers(@RequestBody String id) {
        try {
            membersSvc.delete(id);
            return ResponseUtil.success(Constants.SUCCESS_MSG_DELETED);
        } catch (Exception error) {
            return ResponseUtil.exception(error);
        }
    }
    
    

}
