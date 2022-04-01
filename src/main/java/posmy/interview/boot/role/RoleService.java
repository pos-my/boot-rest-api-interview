package posmy.interview.boot.role;

import java.util.List;

import posmy.interview.boot.entities.Role;
import posmy.interview.boot.exceptions.RecordNotFoundException;

public interface RoleService {

    List<Role> viewAll();

    Role view(String id) throws RecordNotFoundException;
}