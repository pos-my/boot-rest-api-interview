package posmy.interview.boot.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import posmy.interview.boot.dao.Users;

public interface UsersRepo extends JpaRepository<Users, Long> {
  Users findByUsrIdIgnoreCase(String usrId);
  List<Users> findByNameIgnoreCase(String name);
  List<Users> findByRoleIgnoreCase(String role);
  Users findByUsrIdIgnoreCaseAndRoleIgnoreCase(String usrId, String role);
  
  
}
