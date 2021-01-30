package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.BookRecordEntity;

@Repository
public interface BookRecordRepository extends JpaRepository<BookRecordEntity, Long> {
    BookRecordEntity findByMemberIdAndBookId(long memberId, long bookId);
}
