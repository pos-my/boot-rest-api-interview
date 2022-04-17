package posmy.interview.boot.controller;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.service.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;

	@GetMapping(value = "/books/borrow/{id}")
	public ResponseEntity<Object> borrowBook(@PathVariable @Min(1) Long id, Authentication authentication) {
		
		return memberService.borrowBook(id, authentication.getName());
	}
	
	@GetMapping(value = "/books/return/{id}")
	public ResponseEntity<Object> returnBook(@PathVariable @Min(1) Long id, Authentication authentication) {
		return memberService.returnBook(id, authentication.getName());

	}
	
	@GetMapping(value = "/books/get/{id}")
	public ResponseEntity<Object> viewBook(@PathVariable @Min(1) Long id) {
		return memberService.viewBook(id);

	}
	
	@GetMapping(value = "/accounts/info")
	public ResponseEntity<Object> viewMember(Authentication authentication) {
		return memberService.viewMember(authentication.getName());

	}
	
	@GetMapping(value = "/accounts/deactivate")
	public ResponseEntity<Object> deactivateAccount(Authentication authentication) {
		return memberService.deactivateAccount(authentication.getName());
	}
}
