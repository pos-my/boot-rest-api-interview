package posmy.interview.boot.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.entity.User;
import posmy.interview.boot.response.BaseServiceResponse;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/v1/members")
public class MemberController {
	
//	private final String tagName = "User Management";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;
	
	@GetMapping( path = "", produces = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('MEMBER')")
	@ResponseBody
	public ResponseEntity<BaseServiceResponse> viewAccount() throws URISyntaxException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
		BaseServiceResponse result = userService.getUserByUsername( username );
        if ( !result.isSuccess()) {
			return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( result );
		}
        User user = (User) result.getResult();
		return ResponseEntity.ok().body( result );
	}
	
	@GetMapping( path = "/books", produces = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('MEMBER')")
	@ResponseBody
	public ResponseEntity<BaseServiceResponse> viewOnHandBooks() throws URISyntaxException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
		BaseServiceResponse result = bookService.getBookByBorrower( username );
        if ( !result.isSuccess()) {
			return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( result );
		}
        User user = (User) result.getResult();
		return ResponseEntity.ok().body( result );
	}
	
	@DeleteMapping( path = "", produces = MediaType.APPLICATION_JSON_VALUE )
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<BaseServiceResponse> deleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        BaseServiceResponse result = userService.deleteUserByUsername(username);
        return ResponseEntity.accepted().body( result );
    }
}
