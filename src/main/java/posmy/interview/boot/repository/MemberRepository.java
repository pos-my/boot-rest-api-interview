package posmy.interview.boot.repository;

import org.springframework.data.repository.CrudRepository;
import posmy.interview.boot.model.Books;
import posmy.interview.boot.model.Member;

public interface MemberRepository extends CrudRepository<Member, Integer>  {
}
