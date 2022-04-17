package posmy.interview.boot.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import posmy.interview.boot.constants.Constants;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.MemberDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.Member;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.MemberRepository;
import posmy.interview.boot.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseEntity<Object> borrowBook(Long id, String borrowerName) {
		Optional<Book> bookOpt = bookRepository.findById(id);

		if(bookOpt.isPresent()) {
			Book borrowBook = bookOpt.get();
			
			if(Objects.equals(Constants.BOOK_BORROWED, borrowBook.getStatus())) {
				return ResponseEntity.badRequest().body("Book borrowed by: " + borrowBook.getBorrowerName());
			}else if(!StringUtils.hasText(borrowerName)) {
				return ResponseEntity.badRequest().body("Borrower name is mandatory");
			}else {
				borrowBook.setStatus(Constants.BOOK_BORROWED);
				borrowBook.setBorrowerName(borrowerName);
				
				return ResponseEntity.ok(convertToBookDto(bookRepository.save(borrowBook)));
			}
		}
		
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Object> returnBook(Long id, String borrowerName) {
		Optional<Book> bookOpt = bookRepository.findById(id);

		if(bookOpt.isPresent()) {
			Book returnBook = bookOpt.get();
			
			if(Objects.equals(returnBook.getBorrowerName(), borrowerName)) {
				returnBook.setStatus(Constants.BOOK_AVAILABLE);
				returnBook.setBorrowerName(null);

				return ResponseEntity.ok(convertToBookDto(bookRepository.save(returnBook)));
			}else {
				return ResponseEntity.badRequest().build();
			}
		}
		
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Object> viewBook(Long id) {
		Optional<Book> bookOpt = bookRepository.findById(id);

		if(bookOpt.isPresent()) {
			return ResponseEntity.ok(convertToBookDto(bookOpt.get()));
		}
		
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Object> deactivateAccount(String memberName) {
		Optional<Member> memberOpt = memberRepository.findByFullName(memberName);
		
		if(memberOpt.isPresent()) {
			Member member = memberOpt.get();
			member.setStatus(Constants.STATUS_DELETED);
			
			return ResponseEntity.ok(convertToMemberDto(memberRepository.save(member)));
		}

		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Object> viewMember(String memberName) {
		Optional<Member> memberOpt = memberRepository.findByFullName(memberName);

		if(memberOpt.isPresent()) {
			return ResponseEntity.ok(convertToMemberDto(memberRepository.save(memberOpt.get())));
		}

		return ResponseEntity.noContent().build();
	}
	
	private BookDto convertToBookDto(Book book) {
		BookDto bookDto = modelMapper.map(book, BookDto.class);
	   
	    return bookDto;
	}
	
	private MemberDto convertToMemberDto(Member member) {
		MemberDto memberDto = modelMapper.map(member, MemberDto.class);
	   
	    return memberDto;
	}

}
