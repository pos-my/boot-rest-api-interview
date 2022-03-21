package posmy.interview.boot.db;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.entity.BookEntity;

public interface BookDal extends JpaRepository<BookEntity, String> {
}
