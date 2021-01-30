package posmy.interview.boot.service.bookrecord;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.BookRecordEntity;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.BookRecordRequest;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.repository.BookRecordRepository;
import posmy.interview.boot.service.book.BookService;
import posmy.interview.boot.service.member.MemberService;

@Service
@RequiredArgsConstructor
public class BookRecordServiceImpl implements BookRecordService {

    private final BookService bookService;
    private final MemberService memberService;
    private final BookRecordRepository bookRecordRepository;

    @Override
    public long borrowBook(BookRecordRequest bookRecordRequest) throws NoDataFoundException {
        Book book = bookService.getBook(bookRecordRequest.getBookId());
        Member member = memberService.getMember(bookRecordRequest.getMemberId());
        validateBorrowRequest(book, member);
        bookService.updateBookStatus(bookRecordRequest.getBookId(), BookStatus.NOT_AVAILABLE);

        BookRecordEntity bookRecordEntity = new BookRecordEntity();
        bookRecordEntity.setBookId(bookRecordRequest.getBookId());
        bookRecordEntity.setMemberId(bookRecordRequest.getMemberId());
        bookRecordRepository.save(bookRecordEntity);
        return bookRecordEntity.getBookId();
    }

    private void validateBorrowRequest(Book book, Member member) throws NoDataFoundException {
        if(BookStatus.NOT_AVAILABLE.name().equals(book.getStatus())) {
            throw new NoDataFoundException("The book is not available");
        }
        if(member == null) {
            throw new NoDataFoundException("There is no such member");
        }
    }

    @Override
    public void returnBook(BookRecordRequest bookRecordRequest) throws NoDataFoundException {
        Book book = bookService.getBook(bookRecordRequest.getBookId());
        if(BookStatus.AVAILABLE.name().equals(book.getStatus())) {
            throw new NoDataFoundException("The book is still available");
        }
        bookService.updateBookStatus(bookRecordRequest.getBookId(), BookStatus.AVAILABLE);
        BookRecordEntity bookRecordEntity = bookRecordRepository.findByMemberIdAndBookId(bookRecordRequest.getMemberId(), bookRecordRequest.getBookId());
        bookRecordRepository.delete(bookRecordEntity);
    }
}
