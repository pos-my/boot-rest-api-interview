package posmy.interview.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import posmy.interview.boot.constant.BookStatus;
import posmy.interview.boot.model.rest.BookDetail;
import posmy.interview.boot.model.rest.BookRequest;
import posmy.interview.boot.model.rest.BookResponse;
import posmy.interview.boot.model.rest.GetBookResponse;
import posmy.interview.boot.model.rest.GetMemberRequest;
import posmy.interview.boot.model.rest.GetMemberResponse;
import posmy.interview.boot.model.rest.MemberDetail;
import posmy.interview.boot.model.rest.MemberRequest;
import posmy.interview.boot.model.rest.MemberResponse;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = Application.class
		)
class ApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    
    @Value("${library.test.member.user}")
    private String memberUser;
    
    @Value("${library.test.member.password}")
    private String memberPassword;
    
    @Value("${library.test.librarian.user}")
    private String librarianUser;
    
    @Value("${library.test.librarian.password}")
    private String librarianPassword;
 
    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();

    }
    

    @Test
    @DisplayName("Users must be authorized in order to perform actions")
    void contextLoads() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    //Member Role User able to view books
    void testViewBooksWithMemberRole() throws Exception {
    	//Initialize rest response
    	GetBookResponse response = new GetBookResponse();
    	List<BookDetail> bookDetailList = new ArrayList<>();
    	BookDetail bookDetail = new BookDetail();
    	BookDetail bookDetail1 = new BookDetail();
    	
    	//Set rest response
    	bookDetail.setBookId(Integer.toUnsignedLong(1));
    	bookDetail.setBookName("Harry Potter");
    	bookDetail.setStatus(BookStatus.AVAILABLE.name());
    	
    	bookDetail1.setBookId(Integer.toUnsignedLong(2));
    	bookDetail1.setBookName("Hunger Games");
    	bookDetail1.setStatus(BookStatus.AVAILABLE.name());
    	
    	bookDetailList.add(bookDetail);
    	bookDetailList.add(bookDetail1);
    	
    	response.setBook(bookDetailList);
    	
    	//response to json
    	String responseJson = new Gson().toJson(response);
    	
    	mvc.perform(
    			get("/book/viewBooks")
    				.contentType(MediaType.APPLICATION_JSON)
    				.with(httpBasic(memberUser,memberPassword))
    			)
    		.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
   //Member Role User able to view books by bookId
    void testViewBooksByIdWithMemberRole() throws Exception{
    	//Initialize rest request and response
    	BookRequest restRequest = new BookRequest();
    	
    	BookResponse response = new BookResponse();
    	List<BookDetail> bookDetailList = new ArrayList<>();
    	BookDetail bookDetail = new BookDetail();
    	
    	//Set rest request and response
    	restRequest.setId(Integer.toUnsignedLong(1));
    	
    	bookDetail.setBookId(Integer.toUnsignedLong(1));
    	bookDetail.setBookName("Harry Potter");
    	bookDetail.setStatus(BookStatus.AVAILABLE.name());
    	
    	bookDetailList.add(bookDetail);
    	
    	response.setBookDetail(bookDetail);
    	
    	//request and response to json
    	String requestJson = new Gson().toJson(restRequest);
    	String responseJson = new Gson().toJson(response);
    	
    	mvc.perform(
    			get("/book/viewBooksById")
    				.with(httpBasic(memberUser,memberPassword))
    				.contentType(MediaType.APPLICATION_JSON)
    				.content(requestJson)
    			)
    		.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    		
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void borrowBooksWithMemberRole() throws Exception {
    	//Initialize rest request and response
    	BookRequest restRequest = new BookRequest();
    	BookResponse restResponse = new BookResponse();
    	BookDetail bookDetail = new BookDetail();
    	
    	//Set rest request and response
    	restRequest.setId(Integer.toUnsignedLong(1));
    	
    	bookDetail.setBookId(Integer.toUnsignedLong(1));
    	bookDetail.setBookName("Harry Potter");
    	bookDetail.setStatus(BookStatus.BORROWED.name());
    	
    	restResponse.setBookDetail(bookDetail);
    	
    	//response and request to json
    	String requestJson = new Gson().toJson(restRequest);
    	String responseJson = new Gson().toJson(restResponse);
    	
    	mvc.perform(
    			get("/book/borrowBooks")
    				.with(httpBasic(memberUser,memberPassword))
    				.contentType(MediaType.APPLICATION_JSON)
    				.content(requestJson)
    			)
			.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    	
    	
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void returnBooksWithMemberRole() throws Exception {
    	//Initialize rest request and response
    	BookRequest restRequest = new BookRequest();
    	BookResponse restResponse = new BookResponse();
    	BookDetail bookDetail = new BookDetail();
    	
    	//Set rest request and response
    	restRequest.setId(Integer.toUnsignedLong(1));
    	
    	bookDetail.setBookId(Integer.toUnsignedLong(1));
    	bookDetail.setBookName("Harry Potter");
    	bookDetail.setStatus(BookStatus.AVAILABLE.name());
    	
    	restResponse.setBookDetail(bookDetail);
    	
    	//request and response to json
    	String requestJson = new Gson().toJson(restRequest);
    	String responseJson = new Gson().toJson(restResponse);
    	
    	mvc.perform(
    			get("/book/returnBooks")
    				.with(httpBasic(memberUser,memberPassword))
    				.contentType(MediaType.APPLICATION_JSON)
    				.content(requestJson)
    			)
			.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    	
    	
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void addBooksWithLibrarianRole() throws Exception{
    	//Initialize rest request and response
    	BookRequest restRequest = new BookRequest();
    	BookResponse restResponse = new BookResponse();
    	
    	//Set rest request and response
    	restRequest.setId(Integer.toUnsignedLong(3));
    	restRequest.setBookName("Wonderland");
    	restRequest.setStatus(BookStatus.AVAILABLE.name());
    	
    	BookDetail bookDetail = new BookDetail();
    	bookDetail.setBookId(Integer.toUnsignedLong(3));
    	bookDetail.setBookName("Wonderland");
    	bookDetail.setStatus(BookStatus.AVAILABLE.name());
    	
    	restResponse.setBookDetail(bookDetail);
    	
    	//request and response to json
    	String requestJson = new Gson().toJson(restRequest);
    	String responseJson = new Gson().toJson(restResponse);
    	
    	mvc.perform(
    			post("/book/addBooks")
    				.with(httpBasic(librarianUser,librarianPassword))
    				.contentType(MediaType.APPLICATION_JSON)
    				.content(requestJson)
    			)
			.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void updateBooksWithLibrarianRole() throws Exception{
    	//Initialize rest request and response
    	BookRequest restRequest = new BookRequest();
    	BookResponse restResponse = new BookResponse();
    	
    	//Set rest request and response
    	restRequest.setId(Integer.toUnsignedLong(1));
    	restRequest.setBookName("Harry Potter 2");
    	restRequest.setStatus(BookStatus.AVAILABLE.name());
    	
    	BookDetail bookDetail = new BookDetail();
    	bookDetail.setBookId(Integer.toUnsignedLong(1));
    	bookDetail.setBookName("Harry Potter 2");
    	bookDetail.setStatus(BookStatus.AVAILABLE.name());
    	
    	restResponse.setBookDetail(bookDetail);
    	
    	//request and response to json
    	String requestJson = new Gson().toJson(restRequest);
    	String responseJson = new Gson().toJson(restResponse);
    	
    	mvc.perform(
    			post("/book/updateBooks")
    				.with(httpBasic(librarianUser,librarianPassword))
    				.contentType(MediaType.APPLICATION_JSON)
    				.content(requestJson)
    			)
			.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void deleteBooksWithLibrarianRole() throws Exception{
    	//Initialize rest request
    	BookRequest restRequest = new BookRequest();
    	
    	//Set rest request
    	restRequest.setId(Integer.toUnsignedLong(1));
    	restRequest.setBookName("Harry Potter");
    	restRequest.setStatus(BookStatus.AVAILABLE.name());
    	
    	String requestJson = new Gson().toJson(restRequest);
    	
    	mvc.perform(
    			delete("/book/deleteBooks")
    				.with(httpBasic(librarianUser,librarianPassword))
    				.contentType(MediaType.APPLICATION_JSON)
    				.content(requestJson)
    			)
			.andExpect(status().isOk())
    		.andExpect(content().string("Delete Successfully"));
    }

    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void testViewMembersWithLibrarianRole() throws Exception {
    	//Initialize rest response
    	GetMemberResponse response = new GetMemberResponse();
    	MemberDetail memberDetail = new MemberDetail();
    	List<MemberDetail> memberDetailList = new ArrayList<>();
    	
    	//set rest response
    	memberDetail.setUsername("kajing");
    	memberDetail.setAge(20);
    	memberDetail.setFirstName("ka jing");
    	memberDetail.setLastName("hooi");
    	
    	memberDetailList.add(memberDetail);
    	
    	response.setMember(memberDetailList);
    	
    	//response to json
    	String responseJson = new Gson().toJson(response);
    	
    	mvc.perform(
    			get("/member/viewAllMember")
    				.contentType(MediaType.APPLICATION_JSON)
    				.with(httpBasic(librarianUser,librarianPassword))
    			)
    		.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    	
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void testViewMemberByUsernameWithLibrarianRole() throws Exception{
    	//Initialize rest request and response
    	MemberRequest restRequest = new MemberRequest();
    	MemberResponse restResponse = new MemberResponse();
    	MemberDetail memberDetail = new MemberDetail();
    	
    	//Set rest request and response
    	restRequest.setUsername("kajing");
    	
    	memberDetail.setUsername("kajing");
    	memberDetail.setAge(20);
    	memberDetail.setFirstName("ka jing");
    	memberDetail.setLastName("hooi");
    	
    	restResponse.setMemberDetail(memberDetail);
    	
    	//request and response to json
    	String requestJson = new Gson().toJson(restRequest);
    	String responseJson = new Gson().toJson(restResponse);
    	
    	mvc.perform(
    			get("/member/viewMemberByUsername")
    				.contentType(MediaType.APPLICATION_JSON)
    				.with(httpBasic(librarianUser,librarianPassword))
    				.content(requestJson)
    			)
    		.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    	
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void testAddMemberWithLibrarianRole() throws Exception {
    	//Initialize rest request and response
    	MemberRequest restRequest = new MemberRequest();
    	
    	MemberResponse restResponse = new MemberResponse();
    	MemberDetail memberDetail = new MemberDetail();
    	
    	//Set rest request and response
    	restRequest.setUsername("gaojie");
    	restRequest.setAge(21);
    	restRequest.setPassword("password");
    	
    	memberDetail.setUsername("gaojie");
    	memberDetail.setAge(21);
    	
    	restResponse.setMemberDetail(memberDetail);
    	
    	//rest request and response to json
    	String requestJson = new Gson().toJson(restRequest);
    	String responseJson = new Gson().toJson(restResponse);
    	
    	mvc.perform(
    			post("/member/addMember")
    				.contentType(MediaType.APPLICATION_JSON)
    				.with(httpBasic(librarianUser,librarianPassword))
    				.content(requestJson)
    			)
    		.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void testUpdateMemberWithLibrarianRole() throws Exception {
    	//Initialize rest request and response
    	MemberRequest restRequest = new MemberRequest();
    	
    	MemberResponse response = new MemberResponse();
    	MemberDetail memberDetail = new MemberDetail();
    	
    	//Set rest request and response
    	restRequest.setUsername("kajing");
    	restRequest.setAge(33);
    	
    	memberDetail.setUsername("kajing");
    	memberDetail.setAge(33);
    	
    	response.setMemberDetail(memberDetail);
    	
    	//rest request and response to json
    	String requestJson = new Gson().toJson(restRequest);
    	String responseJson = new Gson().toJson(response);
    	
    	response.setMemberDetail(memberDetail);
    	
    	mvc.perform(
    			post("/member/updateMember")
    				.contentType(MediaType.APPLICATION_JSON)
    				.with(httpBasic(librarianUser,librarianPassword))
    				.content(requestJson)
    			)
    		.andExpect(status().isOk())
    		.andExpect(content().json(responseJson));
    }
    
    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void testDeleteMemberWithLibrarianRole() throws Exception {
    	//Initialize rest request
    	GetMemberRequest restRequest = new GetMemberRequest();
    	
    	//Set rest request
    	restRequest.setUsername("kajing");
    	
    	//rest request to json
    	String requestJson = new Gson().toJson(restRequest);
    	
    	mvc.perform(
    			delete("/member/deleteMember")
    				.contentType(MediaType.APPLICATION_JSON)
    				.with(httpBasic(librarianUser,librarianPassword))
    				.content(requestJson)
    			)
    		.andExpect(status().isOk())
    		.andExpect(content().string("Delete Successfully"));
    }

    @Test
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    void testDeleteSelfMemberWithMemberRole() throws Exception{
    	//Initialize rest request
    	GetMemberRequest restRequest = new GetMemberRequest();
    	
    	//Set rest request
    	restRequest.setUsername("kajing");
    	
    	//rest request to json
    	String requestJson = new Gson().toJson(restRequest);
    	
    	mvc.perform(
    			delete("/member/deleteMember")
    				.contentType(MediaType.APPLICATION_JSON)
    				.with(httpBasic(memberUser,memberPassword))
    				.content(requestJson)
    			)
    		.andExpect(status().isOk())
    		.andExpect(content().string("Delete Successfully"));
    	
    }
    
}
