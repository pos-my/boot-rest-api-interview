package posmy.interview.boot.service;

import org.springframework.http.ResponseEntity;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.MemberDto;

public interface LibraryService {
	ResponseEntity<Object> addBook(BookDto bookDto);
	ResponseEntity<Object> updateBook(Long id, BookDto bookDto);
	ResponseEntity<Object> removeBook(Long id);
	
	ResponseEntity<Object> addMember(MemberDto memberDto);
	ResponseEntity<Object> updateMember(Long id, MemberDto memberDto);
	ResponseEntity<Object> removeMember(Long id);
	ResponseEntity<Object> viewMember(Long id);
}
