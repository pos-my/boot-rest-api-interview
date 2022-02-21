package posmy.interview.boot.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.Exception.GenericException;
import posmy.interview.boot.Exception.UncaughtException;
import posmy.interview.boot.Model.dto.BorrowBookReq;
import posmy.interview.boot.Model.dto.GetBookResp;
import posmy.interview.boot.Services.BookService;
import posmy.interview.boot.Services.MemberService;

import javax.validation.Valid;

import static posmy.interview.boot.Constant.UNCAUGHT_EXCEPTION;

@Slf4j
@RestController
@RequestMapping("member")
public class MemberController {

    private final MemberService memberService;
    private final BookService bookService;

    public MemberController(MemberService memberService, BookService bookService) {
        this.memberService = memberService;
        this.bookService = bookService;
    }

    //View
    @GetMapping("view/{id}")
    public GetBookResp viewBook(@PathVariable("id") Long id) {
        try {
            log.info("Retrieve book request, BookId {}", id);
            var book = bookService.getBook(id);

            return GetBookResp.builder()
                    .bookName(book.getBookName())
                    .bookId(book.getBookId())
                    .status(book.getStatus())
                    .borrower(book.getBorrower())
                    .build();
        } catch (GenericException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getMessage(), ex);
        }

    }

    //Borrow
    @PutMapping("borrow/{id}")
    public void borrowBook(@PathVariable("id") Long id, @Valid @RequestBody BorrowBookReq request) {
        try {
            log.info("Member borrow book request, memberId: {}, bookId: {}", request.getMemberId(), id);
            bookService.borrowBook(id, request.getMemberId());
        } catch (GenericException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getMessage(), ex);
        }
    }


    @PutMapping("return/{id}")
    public void returnBook(@PathVariable("id") Long id) {
        try {
            log.info("Member return book, bookId: {}", id);
            bookService.returnBook(id);
        } catch (GenericException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("delete/{username}")
    public void deleteOwnAccount(@PathVariable("username") String username) {
        try {
            log.info("Member remove own account request, account username: {}", username);
            memberService.deleteOwnAccount(username);
        } catch (GenericException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getMessage(), ex);
        }
    }


}
