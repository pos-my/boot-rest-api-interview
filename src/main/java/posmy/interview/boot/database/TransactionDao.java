package posmy.interview.boot.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.model.database.BookEntity;
import posmy.interview.boot.model.database.TransactionEntity;

@Repository
public interface TransactionDao extends JpaRepository<TransactionEntity, Long> {

    @Query(value = "SELECT t FROM TransactionEntity t ")
    Page<TransactionEntity> getTransactions(Pageable pageable);
}
