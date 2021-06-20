package posmy.interview.boot.member;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import posmy.interview.boot.book.Book;

@Controller
@RequestMapping("/api/member")
public class MemberController {
	
	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	// Book related

	@PostMapping("/{memberId}/book/{isbn}")
	public ResponseEntity<Boolean> borrowBook(@PathParam("isbn") Long isbn, @PathParam("member") Long memberId) {
		boolean isSuccess = memberService.borrowBook(isbn, memberId);
		if (!isSuccess) {
			return new ResponseEntity<>(isSuccess, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(isSuccess, HttpStatus.OK);
	}

	@PutMapping("/{memberId}/book/{isbn}")
	public ResponseEntity<Boolean> returnBook(@PathParam("isbn") Long isbn, @PathParam("member") Long memberId) {
		boolean isSuccess = memberService.returnBook(isbn, memberId);
		if (!isSuccess) {
			return new ResponseEntity<>(isSuccess, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(isSuccess, HttpStatus.OK);
	}

	// Member related
	
	@DeleteMapping("/{memberId}")
	public ResponseEntity<Boolean> deleteOwnAccount(@PathParam("memberId") Long memberId) {
		boolean isSuccess = memberService.deleteAccount(memberId);
		if (!isSuccess) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(isSuccess, HttpStatus.OK);
	}

}
