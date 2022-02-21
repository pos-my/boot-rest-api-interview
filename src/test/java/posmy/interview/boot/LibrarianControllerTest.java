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
import posmy.interview.boot.Model.dto.AddBookReq;
import posmy.interview.boot.Model.dto.AddBookResp;
import posmy.interview.boot.Model.dto.AddMemberReq;
import posmy.interview.boot.Model.dto.AddMemberResp;
import posmy.interview.boot.Model.dto.GetMemberResp;
import posmy.interview.boot.Model.dto.UpdateBookReq;
import posmy.interview.boot.Model.dto.UpdateMemberReq;
import posmy.interview.boot.Util.JsonConverter;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class LibrarianControllerTest {

    @Autowired
    private WebApplicationContext context;

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
        mvc.perform(get("/librarian/member/get/jin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("User must have authority to perform actions")
    @WithMockUser(authorities = {"MEMBER"})
    void test2() throws Exception {
        mvc.perform(get("/librarian/member/get/jin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "USER", authorities = {"LIBRARIAN"})
    @DisplayName(("Librarian add new member"))
    void test3() throws Exception {

        //Expected Response
        AddMemberResp expectedResult = AddMemberResp.builder()
                .memberId(001L)
                .username("test01")
                .password("test01")
                .build();

        //Mock Result for Repository
        Users users = Users.builder()
                .userId(001L)
                .username("test01")
                .password("test01")
                .enabled(1)
                .role("MEMBER")
                .build();
        when(usersRepository.save(any(Users.class))).thenReturn(users);

        //Build Request to test
        AddMemberReq request = AddMemberReq.builder()
                .username("test01")
                .password("test01")
                .build();

        var mvcResult = mvc.perform(post("/librarian/member/add")
                .characterEncoding("utf-8")
                .content(JsonConverter.fromObject(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals(JsonConverter.fromObject(expectedResult), mvcResult.getResponse().getContentAsString());
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    @WithMockUser(value = "USER", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian Update member info")
    void test4() throws Exception {

        //Mock Result for Repository
        Users users = Users.builder()
                .userId(001L)
                .username("test01")
                .password("test01")
                .enabled(1)
                .role("MEMBER")
                .build();
        when(usersRepository.findByUsername("test01")).thenReturn(Optional.of(users));

        UpdateMemberReq request = UpdateMemberReq.builder()
                .enabled("0")
                .password("test02")
                .username("test02")
                .build();

        var mcvResult = mvc.perform(put("/librarian/member/update/test01")
                .characterEncoding("utf-8")
                .content(JsonConverter.fromObject(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(usersRepository,times(1)).findByUsername("test01");
        verify(usersRepository,times(1)).save(any(Users.class));
    }

    @Test
    @WithMockUser(value = "USER", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian View member info")
    void test5() throws Exception {
        //Mock Result for Response;
        //Expected Response
        GetMemberResp expectedResult = GetMemberResp.builder()
                .role("MEMBER")
                .enabled(1)
                .username("test01")
                .password("test01")
                .build();

        //Mock Result for Repository
        Users users = Users.builder()
                .userId(001L)
                .username("test01")
                .password("test01")
                .enabled(1)
                .role("MEMBER")
                .build();
        when(usersRepository.findByUsername("test01")).thenReturn(Optional.of(users));

        var mvcResult = mvc.perform(get("/librarian/member/get/test01"))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals(JsonConverter.fromObject(expectedResult), mvcResult.getResponse().getContentAsString());
        verify(usersRepository,times(1)).findByUsername("test01");

    }


    @Test
    @WithMockUser(value = "USER", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian Remove member info")
    void test6() throws Exception {
        Users users = Users.builder()
                .userId(001L)
                .username("test01")
                .password("test01")
                .enabled(1)
                .role("MEMBER")
                .build();
        when(usersRepository.findByUsername("test01")).thenReturn(Optional.of(users));

        var mvcResult = mvc.perform(delete("/librarian/member/delete/test01"))
                .andExpect(status().isOk())
                .andReturn();

        verify(usersRepository,times(1)).findByUsername("test01");
        verify(usersRepository, times(1)).delete(any(Users.class));
    }

    //addBook
    @Test
    @WithMockUser(value = "USER", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian Add new Book")
    void test7() throws Exception {
        //Expected Result
        AddBookResp expectedResult = AddBookResp.builder()
                .bookId(001L)
                .bookName("Book001")
                .status("AVAILABLE")
                .build();

        //Mocked Book
        Book book = Book.builder()
                .bookId(001L)
                .bookName("Book001")
                .status("AVAILABLE")
                .build();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        //Request
        AddBookReq request = new AddBookReq();
        request.setBookName("Book001");

        var mvcResult = mvc.perform(post("/librarian/book/add")
                        .characterEncoding("utf-8")
                        .content(JsonConverter.fromObject(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        Assert.assertEquals(JsonConverter.fromObject(expectedResult), mvcResult.getResponse().getContentAsString());
        verify(bookRepository, times(1)).save(any(Book.class));

    }

    //updateBook
    @Test
    @WithMockUser(value = "USER", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian update existing Book")
    void test8() throws Exception {
        //Mock Result
        Book book = Book.builder()
                .bookId(001L)
                .status("AVAILABLE")
                .bookName("Book001")
                .borrower(null)
                .build();
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));

        //Request
        UpdateBookReq request = UpdateBookReq.builder()
                .bookName("Book001")
                .status("BORROWED")
                .build();
        var mvcResult = mvc.perform(put("/librarian/book/update/1")
                .characterEncoding("utf-8")
                .content(JsonConverter.fromObject(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(bookRepository,times(1)).findById(1L);
        verify(bookRepository,times(1)).save(any(Book.class));
    }

    //deleteBook
    @Test
    @WithMockUser(value = "USER", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian delete existing Book")
    void test9() throws Exception {
        //Mock Result
        Book book = Book.builder()
                .bookId(001L)
                .status("AVAILABLE")
                .bookName("Book001")
                .borrower(null)
                .build();
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));

        var mvcResult = mvc.perform(delete("/librarian/book/delete/1"))
                .andExpect(status().isOk())
                .andReturn();

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).delete(any(Book.class));
    }

}
