package posmy.interview.boot.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import posmy.interview.boot.model.AppUser;
import posmy.interview.boot.model.AppUserRole;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface AppUserService {
    AppUser saveUser(AppUser user) throws UsernameNotFoundException, ValidationException;
    AppUserRole saveUserRole(AppUserRole role);
    boolean addRoleToUser(String username, String roleName) throws ValidationException;
    boolean removeRoleFromUser(String username, String roleName) throws ValidationException;
    AppUser getUser(String username) throws UsernameNotFoundException;
    List<AppUser> getUsers();
    void removeUser(String username) throws UsernameNotFoundException;
}
