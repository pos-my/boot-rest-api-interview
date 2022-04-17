package posmy.interview.boot.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.MemberDto;
import posmy.interview.boot.service.LibraryService;

@RestController
@RequestMapping("/library")
public class LibraryController {
	@Autowired
	private LibraryService libraryService;

	@PostMapping(value = "/books/add")
	public ResponseEntity<Object> addBook(@Valid @RequestBody BookDto bookDto) {

		return libraryService.addBook(bookDto);
	}
	
	@PutMapping(value = "/books/update/{id}")
	public ResponseEntity<Object> updateBook(@PathVariable @Min(1) Long id, @Valid @RequestBody BookDto bookDto) {
		return libraryService.updateBook(id, bookDto);

	}
	
	@DeleteMapping(value = "/books/delete/{id}")
	public ResponseEntity<Object> removeBook(@PathVariable @Min(1) Long id) {
		return libraryService.removeBook(id);
	}
	
	@PostMapping(value = "/members/add")
	public ResponseEntity<Object> addMember(@Valid @RequestBody MemberDto memberDto) {
		return libraryService.addMember(memberDto);

	}
	
	@PutMapping(value = "/members/update/{id}")
	public ResponseEntity<Object> updateMember(@PathVariable @Min(1) Long id, @Valid @RequestBody MemberDto memberDto) {
		return libraryService.updateMember(id, memberDto);

	}
	
	@DeleteMapping(value = "/members/delete/{id}")
	public ResponseEntity<Object> removeMember(@PathVariable @Min(1) Long id) {
		return libraryService.removeMember(id);
	}
	
	@GetMapping(value = "/members/get/{id}")
	public ResponseEntity<Object> getMember(@PathVariable @Min(1) Long id) {
		return libraryService.viewMember(id);
	}
}
