package posmy.interview.boot.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.entity.Role;
import posmy.interview.boot.model.RoleConst;

@Repository
public interface RoleDao extends CrudRepository<Role, Long>{

	Optional<Role> findByName(RoleConst name);

}
