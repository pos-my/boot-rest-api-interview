package posmy.interview.boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

	@Query(value = "SELECT b FROM Book b WHERE b.id = :id")
	Book findBookById(@Param("id") int id);

	@Query(value = "SELECT b FROM Book b WHERE b.status = :status")
	List<Book> findBookAvailable(@Param("status") String status);

	@Modifying
	@Query(value = "UPDATE Book b SET b.name = :name, b.type = :type, b.status = :status, b.user = :userId WHERE b.id = :id ")
	void updateBook(@Param("id") int id, @Param("name") String name, @Param("type") String type,
			@Param("status") String status, @Param("userId") int userId);

	@Query(value = "SELECT b FROM Book b WHERE b.id = :id and b.status = :status")
	Book findBookStatus(@Param("id") int id, @Param("status") String status);

	@Query(value = "SELECT b FROM Book b WHERE b.user = :userId")
	List<Book> findBookUserId(@Param("userId") int userId);

	@Modifying
	@Query(value = "UPDATE Book b SET b.user = :userId, b.status = :status WHERE b.id in (:list) ")
	void borrowBook(@Param("list") List<String> ls, @Param("userId") int userId, @Param("status") String status);

	@Modifying
	@Query(value = "UPDATE Book b SET b.user = null, b.status = :status WHERE b.id in (:list) ")
	void returnBook(@Param("list") List<String> ls, @Param("status") String status);

}
