package posmy.interview.boot.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.entity.User;
import posmy.interview.boot.request.UserRequest;
import posmy.interview.boot.response.BaseServiceResponse;
import posmy.interview.boot.service.UserService;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/v1/admin")
public class AdminController {
	
//	private final String tagName = "User Management";
	
	@Autowired
	private UserService userService;
	
	@GetMapping( path = "/users", produces = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('LIBRARIAN')")
	@ResponseBody
    public ResponseEntity<BaseServiceResponse> getAllUsers( ) throws Exception {
		BaseServiceResponse result = userService.getAllUsers();
		if( !result.isSuccess() ) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body( result );
		} 
		return ResponseEntity.ok().body( result );
    }
	
	@GetMapping( path = "/users/{id:.+}", produces = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('LIBRARIAN')")
	@ResponseBody
	public ResponseEntity<BaseServiceResponse> getUserById( @PathVariable("id") @Valid long id ) {
		BaseServiceResponse result = userService.getUserById(id);

		if ( !result.isSuccess()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body( result );
		}
		return ResponseEntity.ok().body( result );
	}
	
	@PostMapping( path = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('LIBRARIAN')")
	@ResponseBody 
	public ResponseEntity<BaseServiceResponse> addUser(@RequestBody @Valid UserRequest request ) throws URISyntaxException {
		BaseServiceResponse result = userService.addUser( request );
        if ( !result.isSuccess()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body( result );
		}
        User user = (User) result.getResult();
		return ResponseEntity.created( new URI( "/users/" + user.getId() ) ).body( result );
	}
	
	@PatchMapping( path = "/users/{id:.+}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('LIBRARIAN')")
	@ResponseBody
	public ResponseEntity<BaseServiceResponse> updateUser( @PathVariable("id") @Valid long id,  @RequestBody @Valid UserRequest request ) throws URISyntaxException {
		BaseServiceResponse result = userService.updateUser( id, request );
		if ( !result.isSuccess()) {
			return ResponseEntity.badRequest().body( result );
		}
        return ResponseEntity.ok().body( result );
	}
	
	@DeleteMapping( path = "/users/{id:.+}", produces = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('LIBRARIAN')")
	@ResponseBody
	public ResponseEntity<BaseServiceResponse> deleteUser( @PathVariable("id") @Valid long id ) {
		BaseServiceResponse result = userService.deleteUser( id );
		if ( !result.isSuccess()) {
			return ResponseEntity.badRequest().body( result );
		}
        return ResponseEntity.accepted().body( result );
	}
	
}
