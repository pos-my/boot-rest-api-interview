package posmy.interview.boot.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.Exception.GenericException;
import posmy.interview.boot.Exception.UncaughtException;
import posmy.interview.boot.Model.dto.AddBookReq;
import posmy.interview.boot.Model.dto.AddBookResp;
import posmy.interview.boot.Model.dto.AddMemberReq;
import posmy.interview.boot.Model.dto.AddMemberResp;
import posmy.interview.boot.Model.dto.GetMemberResp;
import posmy.interview.boot.Model.dto.UpdateBookReq;
import posmy.interview.boot.Model.dto.UpdateMemberReq;
import posmy.interview.boot.Services.BookService;
import posmy.interview.boot.Services.MemberService;

import javax.validation.Valid;

import static posmy.interview.boot.Constant.UNCAUGHT_EXCEPTION;

@Slf4j
@RestController
@RequestMapping("librarian")
public class LibrarianController {

    private final BookService bookService;
    private final MemberService memberService;

    public LibrarianController(BookService bookService, MemberService memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
    }

    //add
    @PostMapping("member/add")
    public AddMemberResp addMember(@Valid @RequestBody AddMemberReq request) {
        try {
            log.info("Receive addMember request: {}", request);
            var member = memberService.addMember(request.getUsername(), request.getPassword());

            return AddMemberResp.builder()
                    .memberId(member.getUserId())
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .build();
        } catch (GenericException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getMessage(), ex);
        }
    }

    //update
    @PutMapping("member/update/{username}")
    public void updateMember(@PathVariable("username") String username, @Valid @RequestBody UpdateMemberReq request) {
        try {
            log.info("Receive update request: {}", request);
            memberService.updateMember(username, request.getUsername(), request.getPassword(), Integer.parseInt(request.getEnabled()), request.getRole());
        } catch (GenericException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getMessage(), ex);
        }
    }

    //view
    @GetMapping("member/get/{username}")
    public GetMemberResp getMember(@PathVariable("username") String username) {
        try {
            log.info("Retrieve member request, username: {}", username);
            var member = memberService.getMember(username);

            return GetMemberResp.builder()
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .enabled(member.getEnabled())
                    .role(member.getRole())
                    .build();
        } catch (GenericException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getMessage(), ex);
        }
    }

    //remove
    @DeleteMapping("member/delete/{username}")
    public void deleteMember(@PathVariable("username") String username) {
        try {
            log.info("Remove member request, username: {}", username);
            memberService.deleteMemberAccount(username);
        } catch (GenericException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getMessage(), ex);
        }
    }

    @PostMapping("book/add")
    public AddBookResp addBook(@Valid @RequestBody AddBookReq request) {
        try {
            log.info("Receive add book request: {}", request);
            var book = bookService.addBook(request.getBookName());

            return AddBookResp.builder()
                    .bookName(book.getBookName())
                    .bookId(book.getBookId())
                    .status(book.getStatus())
                    .build();
        } catch (GenericException ex) {
            throw ex;
        } catch (UncaughtException ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getErrorMessage(), ex);
        }
    }

    @PutMapping("book/update/{id}")
    public void updateBookResp(@PathVariable("id") Long id, @Valid @RequestBody UpdateBookReq request) {
        try {
            log.info("Update book request, BookId:{}, requestBody: {}", id, request);
            bookService.updateBook(id, request.getBorrower(), request.getBookName(), request.getStatus());

        } catch (GenericException ex) {
            throw ex;
        } catch (UncaughtException ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getErrorMessage(), ex);
        }
    }

    @DeleteMapping("book/delete/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        try {
            log.info("Delete book ruquest, BookId: {}", id);
            bookService.removeBook(id);
        } catch (GenericException ex) {
            throw ex;
        } catch (UncaughtException ex) {
            throw new UncaughtException(UNCAUGHT_EXCEPTION, ex.getErrorMessage(), ex);
        }
    }

}
