package posmy.interview.boot.role;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.entities.Role;
import posmy.interview.boot.exceptions.RecordNotFoundException;
import posmy.interview.boot.repositories.RoleRepository;
import posmy.interview.boot.security.BaseSecurityService;

@Service
public class RoleServiceImpl extends BaseSecurityService implements RoleService {

    @Autowired
    @NotNull
    private RoleRepository roleRepo;

    public List<Role> viewAll() {
        return roleRepo.findAll();
    }

    public Role view(String name) throws RecordNotFoundException {
        Role role = new Role();
        try {
            role = roleRepo.findByName(name);
        } catch (Exception e) {
            throw new RecordNotFoundException("Role does not exists");
        }
        return role;
    }
}
