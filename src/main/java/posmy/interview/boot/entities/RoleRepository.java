package posmy.interview.boot.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByName(String string);

}
