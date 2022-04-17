package posmy.interview.boot.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import posmy.interview.boot.constants.Constants;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.MemberDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.Member;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.MemberRepository;
import posmy.interview.boot.service.LibraryService;

@Service
public class LibraryServiceImpl implements LibraryService{

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseEntity<Object> addBook(BookDto bookDto) {
		
		bookDto.setStatus(Constants.BOOK_AVAILABLE);

		Book newBook = convertToBookEntity(bookDto);
		
		Book createdBook = bookRepository.save(newBook);
		
		return ResponseEntity.ok(convertToBookDto(createdBook));
	}
	
	@Override
	public ResponseEntity<Object> updateBook(Long id, BookDto bookDto) {
		if(!Objects.equals(id, bookDto.getId())){
			return ResponseEntity.badRequest().build();
		}
		
		Optional<Book> bookOpt = bookRepository.findById(id);

		if(bookOpt.isPresent()){
			Book existingBook = bookOpt.get();
			
			Book sourceBook = convertToBookEntity(bookDto);
			modelMapper.map(sourceBook, existingBook);

			existingBook = bookRepository.save(existingBook);
			
			return ResponseEntity.ok(convertToBookDto(existingBook));
		}

		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Object> removeBook(Long id) {
		Optional<Book> bookOpt = bookRepository.findById(id);

		if(bookOpt.isPresent()){
			Book book = bookOpt.get();
			book.setStatus(Constants.STATUS_DELETED);
			
			Book deletedBook = bookRepository.save(book);

			return ResponseEntity.ok(convertToBookDto(deletedBook));
		}

		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Object> addMember(MemberDto memberDto) {
		memberDto.setStatus(Constants.STATUS_ACTIVE);
		Member member = convertToMemberEntity(memberDto);
		Member createdMember = memberRepository.save(member);

		return ResponseEntity.ok(convertToMemberDto(createdMember));
	}

	@Override
	public ResponseEntity<Object> updateMember(Long id, MemberDto memberDto) {
		if(!Objects.equals(id, memberDto.getId())){
			return ResponseEntity.badRequest().build();
		}
		
		Optional<Member> memberOpt = memberRepository.findById(id);

		if(memberOpt.isPresent()) {
			Member existingMember = memberOpt.get();
			
			Member souceMember = convertToMemberEntity(memberDto);
			modelMapper.map(souceMember, existingMember);
			
			existingMember = memberRepository.save(existingMember);
		
			return ResponseEntity.ok(convertToMemberDto(existingMember));
		}

		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Object> removeMember(Long id) {
		Optional<Member> memberOpt = memberRepository.findById(id);

		if(memberOpt.isPresent()) {
			Member member = memberOpt.get();
			member.setStatus(Constants.STATUS_DELETED);

			return ResponseEntity.ok(memberRepository.save(member));
		}

		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Object> viewMember(Long id) {
		Optional<Member> member = memberRepository.findById(id);

		if(member.isPresent()) {
			return ResponseEntity.ok(convertToMemberDto(member.get()));
		}

		return ResponseEntity.noContent().build();
	}
	
	private Book convertToBookEntity(BookDto bookDto)  {
		
		Book book = modelMapper.map(bookDto, Book.class);
	    
	    return book;
	}
	
	private BookDto convertToBookDto(Book book) {
		BookDto bookDto = modelMapper.map(book, BookDto.class);
	   
	    return bookDto;
	}
	
	private Member convertToMemberEntity(MemberDto memberDto)  {
		
		Member member = modelMapper.map(memberDto, Member.class);

	    return member;
	}
	
	private MemberDto convertToMemberDto(Member member) {
		MemberDto memberDto = modelMapper.map(member, MemberDto.class);
	   
	    return memberDto;
	}
}

