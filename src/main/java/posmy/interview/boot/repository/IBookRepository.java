package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import posmy.interview.boot.entity.Book;

public interface IBookRepository extends JpaRepository<Book,Long>{

}
