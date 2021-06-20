package posmy.interview.boot.member;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import posmy.interview.boot.book.Book;
import posmy.interview.boot.book.BookService;
import posmy.interview.boot.entities.MemberRepository;

@Service
public class MemberService {
	
	private final BookService bookService;
	private final MemberRepository memberRepository;
	
	public MemberService(MemberRepository memberRepository, BookService bookService) {
		this.bookService = bookService;
		this.memberRepository = memberRepository;
	}

	public boolean addMembers(List<Member> members) {
		if(members.stream().allMatch(member -> memberRepository.findById(member.getId()).isPresent())) {
			return false;
		}
		members.stream().map(member -> {
			posmy.interview.boot.entities.Member result = new posmy.interview.boot.entities.Member();
			result.setId(member.getId());
			result.setUsername(member.getUsername());
			result.setPassword(member.getPassword());
			return result;
		}).forEach(member -> memberRepository.saveAndFlush(member))
		;
		return true;
	}

	public boolean updateMember(Long id, Member member) {
		Optional<posmy.interview.boot.entities.Member> memberInDb = memberRepository.findById(id);
		if(!memberInDb.isPresent()) {
			return false;
		}
		posmy.interview.boot.entities.Member memberDb = memberInDb.get();
		memberDb.setId(member.getId());
		memberDb.setUsername(member.getUsername());
		memberDb.setPassword(member.getPassword());
		memberRepository.saveAndFlush(memberDb);
		return true;
	}

	public Optional<Member> getMember(Long id) {
		Optional<posmy.interview.boot.entities.Member> memberInDb = memberRepository.findById(id);
		if(!memberInDb.isPresent()) {
			return Optional.empty();
		}
		posmy.interview.boot.entities.Member memberDb = memberInDb.get();
		Member member = new Member();
		member.setId(memberDb.getId());
		member.setUsername(memberDb.getUsername());
		member.setPassword(member.getPassword());
		return Optional.ofNullable(member);
	}

	public boolean deleteMember(Long id) {
		if(!memberRepository.findById(id).isPresent()) {
			return false;
		}
		memberRepository.deleteById(id);
		return true;
	}
	
	public boolean borrowBook(Long isbn, Long memberId) {
		return bookService.borrowBook(isbn, memberId);
	}

	public boolean returnBook(Long isbn, Long memberId) {
		return bookService.returnBook(isbn, memberId);
	}

	public boolean deleteAccount(Long memberId) {
		if(!memberRepository.findById(memberId).isPresent()) {
			return false;
		}
		memberRepository.deleteById(memberId);
		return true;
	}
	
}
