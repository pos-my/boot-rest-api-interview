package posmy.interview.boot.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.model.AppUser;
import posmy.interview.boot.model.AppUserRole;
import posmy.interview.boot.repo.AppUserRepo;
import posmy.interview.boot.repo.AppUserRoleRepo;

import javax.xml.bind.ValidationException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepo userRepo;
    private final AppUserRoleRepo userRoleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not exists"));
        log.info("User is found: {}", user);
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public AppUser saveUser(AppUser user) throws ValidationException, UsernameNotFoundException {
        if(user.getId() != null){
            AppUser tempUser = userRepo.findById(user.getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not exists"));

            boolean isLibrarian = tempUser.getRoles().stream()
                    .anyMatch(appUserRole -> "LIBRARIAN".equals(appUserRole.getName()));
            if(isLibrarian){
                throw new ValidationException("Unable to update user with role LIBRARIAN");
            }
            log.info("Updating user {}", user);
        } else {
            log.info("Adding user {}", user);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public AppUserRole saveUserRole(AppUserRole role) {
        log.info("Saving new role {}", role);
        return userRoleRepo.save(role);
    }

    @Override
    public boolean addRoleToUser(String username, String roleName) throws UsernameNotFoundException, ValidationException {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists"));
        AppUserRole role = userRoleRepo.findByName(roleName)
                .orElseThrow(() -> new ValidationException("User role not exists"));

        if(user.getRoles().contains(role)){
            throw new ValidationException(
                    String.format("Role %s already added to user %s", role.getName(), user.getUsername())
            );
        }

        log.info("Adding role {} to user {}", roleName, username);
        return user.getRoles().add(role);
    }

    @Override
    public boolean removeRoleFromUser(String username, String roleName) throws ValidationException {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists"));
        AppUserRole role = userRoleRepo.findByName(roleName)
                .orElseThrow(() -> new ValidationException("User role not exists"));

        if(! user.getRoles().contains(role)){
            throw new ValidationException(
                    String.format("User %s not having role %s", user.getUsername(), role.getName())
            );
        }

        log.info("Removing role {} from user {}", roleName, username);
        return user.getRoles().remove(role);
    }

    @Override
    public AppUser getUser(String username) throws UsernameNotFoundException {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not exists"));
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }

    @Override
    public void removeUser(String username) throws UsernameNotFoundException {
        AppUser user = this.getUser(username);
        log.info("Removing user {}", user);
        userRepo.delete(user);
    }
}
