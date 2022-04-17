package posmy.interview.boot.service;

import org.springframework.http.ResponseEntity;

public interface MemberService {
	ResponseEntity<Object> borrowBook(Long id, String borrowerName);
	ResponseEntity<Object> returnBook(Long id, String borrowerName);
	ResponseEntity<Object> viewBook(Long id);
	
	ResponseEntity<Object> deactivateAccount(String memberName);
	ResponseEntity<Object> viewMember(String memberName);
}
