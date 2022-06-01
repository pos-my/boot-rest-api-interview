package posmy.interview.boot.service.api;

import posmy.interview.boot.model.Role;

import java.util.List;

public interface RoleService {
    /**
     * save all roles
     *
     * @param roles data
     * @return saved data
     */
    List<Role> saveAll(List<Role> roles);

    /**
     * get all roles for user role assign purpose
     *
     * @return all roles data
     */
    List<Role> findAll();
}
