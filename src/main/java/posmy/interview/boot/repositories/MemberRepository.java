package posmy.interview.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.models.daos.Member;
import posmy.interview.boot.models.dtos.member.UserRole;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByFirstName(String firstName);

    List<Member> findByLastName(String lastName);

    Optional<Member> findByPhone(String phone);

    Optional<Member> findByEmail(String email);

    List<Member> findByUserRole(UserRole userRole);
}
