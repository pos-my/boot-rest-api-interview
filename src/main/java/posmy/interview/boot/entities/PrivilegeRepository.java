package posmy.interview.boot.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{

	Privilege findByName(String name);

}
