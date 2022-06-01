package posmy.interview.boot;

import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import posmy.interview.boot.base.TestBase;
import posmy.interview.boot.comparator.RegexNullableValueMatcher;
import posmy.interview.boot.dto.UnitTestRequest;
import posmy.interview.boot.enums.HttpMethod;
import posmy.interview.boot.enums.Role;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.service.api.BookService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookIntegrationTest extends TestBase {

    @Autowired
    BookService bookService;

    List<Book> books = new ArrayList<>();

    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();
        //get book for borrow and return test use
        books.addAll(bookService.findByName("Wolf Hall"));
        books.addAll(bookService.findByName("Gilead"));

    }

    @Test
    @Order(10)
    public void When_noToken_Then_returnInvalidToken() {
        //get un-auth when no token provided
        verifyInvalidToken("/book", HttpMethod.POST);
        verifyInvalidToken("/book/name/Wolf Hall", HttpMethod.GET);
    }

    @Test
    @Order(20)
    public void When_getAllWithoutAccess_Then_returnAccessDenied() {
        //without access right
        verifyAccessDenied("/book", HttpMethod.POST, Role.MEMBER);
        verifyAccessDenied("/book/name/Wolf Hall", HttpMethod.GET, Role.LIBRARIAN);
    }

    @Test
    @Order(30)
    public void When_saveWithAccess_Then_saveAndReturnData() {
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/book/")
                .requestFile("data/book/positive/save.json")
                .responseFile("data/book/positive/saveResponse.json")
                .token(this.getTokenByAccess(Role.LIBRARIAN))
                .statusResult(status().isOk())
                .customComparator(getIdStandardComparator())
                .build());
    }

    @Test
    @Order(40)
    public void When_getUserNameWithAccess_Then_returnData() {
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.GET)
                .url("/book/name/Wolf Hall")
                .responseFile("data/book/positive/nameResponse.json")
                .token(this.getTokenByAccess(Role.MEMBER))
                .statusResult(status().isOk())
                .customComparator(getIdStandardComparator())
                .build());
    }


    @Test
    @Order(50)
    public void When_borrowBookAlreadyNotAvail_Then_returnBookIsBorrowed() {
        //todo: use script to insert a borrowed book with static id in future
        //get book id
        var bookId = books.stream().filter(b -> b.getName().equals("Wolf Hall")).findFirst().get().getId().toString();
        //let member1 borrow 1st
        verifyBorrowBook(bookId, "borrowResponse");
        //get member2 token
        var accessToken = generateToken("member02", List.of("MEMBER"));
        //borrow same book
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/book/borrow")
                .requestFile("data/book/positive/borrow.json")
                .requestContextInvoker((input) -> input.replace("id", bookId))
                .responseFile("data/book/negative/borrowByOthersResponse.json")
                .token(accessToken)
                .statusResult(status().isBadRequest())
                .build());
    }


    @Test
    @Order(60)
    public void When_borrowBookAvail_Then_updateBookBorrowAndReturnData() {
        //get book id
        var bookId = books.stream().filter(b -> b.getName().equals("Gilead")).findFirst().get().getId().toString();
        //borrow book
        verifyBorrowBook(bookId, "borrowResponse02");

    }


    @Test
    @Order(70)
    public void When_returnBook_Then_updateBookBorrowAndReturnData() {
        //todo: need to use script insert a borrowed record in future
        //get book id
        var bookId = books.stream().filter(b -> b.getName().equals("Gilead")).findFirst().get().getId().toString();
        //return book
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/book/return")
                .requestFile("data/book/positive/borrow.json")
                .requestContextInvoker((input) -> input.replace("id", bookId))
                .responseFile("data/book/positive/returnResponse.json")
                .token(this.getTokenByAccess(Role.MEMBER))
                .statusResult(status().isOk())
                .customComparator(getIdStandardComparator())
                .build());

    }

    public CustomComparator getIdAndBorrowByStandardComparator() {
        // as long as id is numeric = fine, regardless under array or node
        return new CustomComparator(JSONCompareMode.STRICT
                , new Customization("**.id", new RegexNullableValueMatcher("\\d+", false))
                , new Customization("**.borrowBy", new RegexNullableValueMatcher("\\d+", false)
        ));
    }

    private void verifyBorrowBook(String bookId, String responseFileName) {
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/book/borrow")
                .requestFile("data/book/positive/borrow.json")
                .requestContextInvoker((input) -> input.replace("id", bookId))
                .responseFile(String.format("data/book/positive/%s.json", responseFileName))
                .token(this.getTokenByAccess(Role.MEMBER))
                .statusResult(status().isOk())
                .customComparator(getIdAndBorrowByStandardComparator())
                .build());
    }


}
