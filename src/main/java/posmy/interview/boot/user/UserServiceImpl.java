package posmy.interview.boot.user;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.entities.Role;
import posmy.interview.boot.entities.User;
import posmy.interview.boot.exceptions.DuplicateRecordException;
import posmy.interview.boot.exceptions.ExceptionConstants;
import posmy.interview.boot.exceptions.RecordNotFoundException;
import posmy.interview.boot.exceptions.UnauthorizedException;
import posmy.interview.boot.repositories.RoleRepository;
import posmy.interview.boot.repositories.UserRepository;
import posmy.interview.boot.security.BaseSecurityService;
import posmy.interview.boot.user.request.NewUserRequest;
import posmy.interview.boot.user.request.UpdateUserRequest;

@Service
public class UserServiceImpl extends BaseSecurityService implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    @NotNull
    private UserRepository userRepo;

    @Autowired
    @NotNull
    private RoleRepository roleRepo;

    public List<User> viewAll() throws UnauthorizedException {
        if (this.isLibrarian()) {
            return userRepo.findAll();
        } else {
            throw new UnauthorizedException(ExceptionConstants.USER_NOT_ALLOWED);
        }

    }

    public User view(Long id) throws RecordNotFoundException, UnauthorizedException {

        Optional<User> user;

        if (this.isLibrarian()) {
            user = userRepo.findById(id);
            if (user.isPresent()) {
                return user.get();
            } else {
                throw new RecordNotFoundException(ExceptionConstants.USER_NOT_FOUND_EXCEPTION);
            }
        } else {
            throw new UnauthorizedException(ExceptionConstants.USER_NOT_ALLOWED);
        }
    }

    public User viewByName(String username) throws RecordNotFoundException, UnauthorizedException {

        if (this.isLibrarian()) {
            User user = userRepo.findByUsername(username);
            if (user != null) {
                return user;
            } else {
                throw new RecordNotFoundException(ExceptionConstants.USER_NOT_FOUND_EXCEPTION);
            }
        } else {
            throw new UnauthorizedException(ExceptionConstants.USER_NOT_ALLOWED);
        }
    }

    public User add(NewUserRequest item) throws DuplicateRecordException {
        User user = this.modelMapper.map(item, User.class);
        if (userRepo.findByUsername(item.getUsername()) != null) {
            throw new DuplicateRecordException("User is exists");
        }
        Role role = roleRepo.findByName(item.getRole());
        user.setUsername(item.getUsername());
        user.setRole(role);
        user.setPassword(item.getPassword());
        userRepo.saveAndFlush(user);
        logger.info("User Added : {}", user);

        return user;
    }

    public User update(Long id, UpdateUserRequest item) throws RecordNotFoundException, UnauthorizedException {
        User old = view(id);
        Role role = roleRepo.findByName(item.getRole());
        old.setPassword(item.getPassword());
        old.setRole(role);
        userRepo.saveAndFlush(old);
        logger.info("User Updated  : {}", old);

        return old;
    }

    public void delete(Long id) throws RecordNotFoundException, UnauthorizedException {
        if (this.isLibrarian()) {
            userRepo.delete(this.view(id));
        } else {
            if (id.equals(this.getCurrentUser().getId())) {
                userRepo.delete(this.view(id));
            } else {
                throw new UnauthorizedException(ExceptionConstants.USER_NOT_ALLOWED);
            }
        }
    }

}
