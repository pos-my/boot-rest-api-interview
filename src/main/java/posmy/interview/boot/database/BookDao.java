package posmy.interview.boot.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.model.database.BookEntity;

@Repository
public interface BookDao extends JpaRepository<BookEntity, Long> {

    @Query(value = "SELECT t FROM BookEntity t WHERE " +
            "t.name like COALESCE(:name, t.name) and  " +
            "t.status = COALESCE(:status, t.status) and " +
            "t.description like COALESCE(:description, t.description)")
    Page<BookEntity> getBooksByNameAndDescriptionAndStatus(
            @Param("name") String name,
            @Param("description") String description,
            @Param("status") String status,
            Pageable pageable);
}
