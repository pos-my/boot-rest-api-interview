package posmy.interview.boot.librarian;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import posmy.interview.boot.book.Book;
import posmy.interview.boot.book.BookService;
import posmy.interview.boot.member.Member;
import posmy.interview.boot.member.MemberService;

@Service
public class LibrarianService {
	
	private final BookService bookService;
	private final MemberService memberService;
	
	public LibrarianService(BookService bookService, MemberService memberService) {
		this.bookService = bookService;
		this.memberService = memberService;
	}
	
	public boolean addBooks(List<Book> books) {
		return bookService.addBooks(books);
	}

	public boolean updateBook(Long isbn, Book book) {
		return bookService.updateBook(isbn, book);
	}

	public boolean deleteBook(Long isbn) {
		return bookService.deleteBook(isbn);
	}

	public boolean addMembers(List<Member> members) {
		return memberService.addMembers(members);
	}

	public boolean updateMember(Long id, Member member) {
		return memberService.updateMember(id, member);
	}

	public Optional<Member> getMember(Long id) {
		return memberService.getMember(id);
	}

	public boolean deleteMember(Long id) {
		return memberService.deleteMember(id);
	}

}
