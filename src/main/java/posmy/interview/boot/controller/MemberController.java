package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.request.GenericReq;
import posmy.interview.boot.service.MemberBookService;
import posmy.interview.boot.service.MemberSelfService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired
    MemberBookService memberBookService;

    @Autowired
    MemberSelfService memberSelfService;

    @GetMapping(value = "/book/list", produces = "application/json")
    public List<Book> getBooks() {
        return memberBookService.viewAll();
    }

    @GetMapping(value = "/book/{id}", produces = "application/json")
    public Optional<Book> getBook(@PathVariable Long id) {
        return memberBookService.get(id);
    }

    @PutMapping(value = "/book/borrow", produces = "application/json")
    public Optional<Book> borrowBook(@RequestBody final GenericReq<Long> req) {
        return memberBookService.borrowBook(req.getBody());
    }

    @PutMapping(value = "/book/return", produces = "application/json")
    public Optional<Book> returnBook(@RequestBody final GenericReq<Long> req) {
        return memberBookService.returnBook(req.getBody());
    }

    @PostMapping(value = "/resign", produces = "application/json")
    public Optional<Member> resign() {
        return memberSelfService.resign();
    }

}
