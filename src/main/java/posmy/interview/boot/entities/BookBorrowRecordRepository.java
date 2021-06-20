package posmy.interview.boot.entities;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import posmy.interview.boot.entities.Book.Status;
import posmy.interview.boot.entities.BookBorrowRecord.Action;

public interface BookBorrowRecordRepository extends JpaRepository<BookBorrowRecord, Long>{

	Optional<BookBorrowRecord> findByBookIsbnAndMemberIdAndAction(Long isbn, Long memberId, Action action);

}
