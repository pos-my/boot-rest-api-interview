package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.BorrowActivity;
import posmy.interview.boot.model.BorrowStatus;

public interface BorrowActivityRepository extends JpaRepository<BorrowActivity, Long> {
    public BorrowActivity findByUserIdAndBookIdAndStatus(Long userId, Long bookId, BorrowStatus status);
}
