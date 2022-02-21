package posmy.interview.boot;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.Model.Book;
import posmy.interview.boot.Model.Repository.BookRepository;
import posmy.interview.boot.Model.Repository.UsersRepository;
import posmy.interview.boot.Model.Users;
import posmy.interview.boot.Model.dto.BorrowBookReq;
import posmy.interview.boot.Model.dto.GetBookResp;
import posmy.interview.boot.Services.MemberService;
import posmy.interview.boot.Util.JsonConverter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MemberService memberService;

    private MockMvc mvc;

    @MockBean
    UsersRepository usersRepository;
    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("Users must be authorized in order to perform actions")
    void test1() throws Exception {
        mvc.perform(get("/member/view/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("User must have authority to perform actions")
    @WithMockUser(authorities = {"LIBRARIAN"})
    void test2() throws Exception {
        mvc.perform(get("/member/view/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName(("Member view book"))
    @WithMockUser(authorities = {"MEMBER"})
    void test3() throws Exception {
        //ExpectedResult
        GetBookResp expectedResult = GetBookResp.builder()
                .bookId(001L)
                .bookName("Book001")
                .status("AVAILABLE")
                .borrower(null)
                .build();

        //Mock Repo
        Book book = Book.builder()
                .bookId(001L)
                .bookName("Book001")
                .status("AVAILABLE")
                .build();
        when(bookRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(book));

        var mvcResult = mvc.perform(get("/member/view/1"))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals(JsonConverter.fromObject(expectedResult), mvcResult.getResponse().getContentAsString());
        verify(bookRepository, times(1)).findById(1L);
    }

    //borrow
    @Test
    @DisplayName("Member borrow book")
    @WithMockUser(authorities = {"MEMBER"})
    void test4() throws Exception {
        //Mock Repo
        Book book = Book.builder()
                .bookId(1L)
                .bookName("Book001")
                .status("AVAILABLE")
                .build();
        when(bookRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(book));
        Book book2 = Book.builder()
                .bookId(1L)
                .bookName("Book001")
                .status("BORROWED")
                .borrower(1L)
                .build();
        when(bookRepository.save(any(Book.class))).thenReturn(book2);

        BorrowBookReq request = new BorrowBookReq();
        request.setMemberId(1L);
        var mvcResult = mvc.perform(put("/member/borrow/1")
                        .characterEncoding("utf-8")
                        .content(JsonConverter.fromObject(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book2);
    }

    //return
    @Test
    @DisplayName("Member return book")
    @WithMockUser(authorities = {"MEMBER"})
    void test5() throws Exception {
        //Mock Repo
        Book book = Book.builder()
                .bookId(1L)
                .bookName("Book001")
                .status("BORROWED")
                .borrower(1L)
                .build();
        when(bookRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(book));
        Book book2 = Book.builder()
                .bookId(1L)
                .bookName("Book001")
                .status("AVAILABLE")
                .borrower(null)
                .build();
        when(bookRepository.save(any(Book.class))).thenReturn(book2);

        var mvcResult = mvc.perform(put("/member/return/1"))
                .andExpect(status().isOk())
                .andReturn();

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book2);
    }

    //deleteAccount
    @Test
    @DisplayName("Member delete Own Account")
    @WithMockUser(username = "jin", authorities = "MEMBER")
    void test6() throws Exception {

        //MockRepo
        Users users = Users.builder()
                .username("jin")
                .password("jin")
                .role("MEMBER")
                .enabled(1)
                .build();
        when(usersRepository.findByUsername("jin")).thenReturn(java.util.Optional.ofNullable(users));

        var mvcResult = mvc.perform(delete("/member/delete/jin"))
                .andExpect(status().isOk())
                .andReturn();

        verify(usersRepository, times(1)).findByUsername("jin");
        verify(usersRepository, times(1)).delete(users);

    }

    //deleteAccount
    @Test
    @DisplayName("Member unable delete Others Account")
    @WithMockUser(username = "jin", authorities = "MEMBER")
    public void test7() throws Exception {
        //MockRepo
        Users users = Users.builder()
                .username("peng")
                .password("peng")
                .role("MEMBER")
                .enabled(1)
                .build();
        when(usersRepository.findByUsername("peng")).thenReturn(java.util.Optional.ofNullable(users));

        var mvcResult = mvc.perform(delete("/member/delete/peng"))
                .andExpect(status().is(1003))
                .andReturn();
    }

}
