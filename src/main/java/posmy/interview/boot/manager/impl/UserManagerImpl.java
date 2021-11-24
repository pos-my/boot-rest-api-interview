package posmy.interview.boot.manager.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import posmy.interview.boot.domain.User;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserStatus;
import posmy.interview.boot.manager.MessageResources;
import posmy.interview.boot.manager.UserManager;
import posmy.interview.boot.repo.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserManagerImpl implements UserManager {
    Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addMember(User user) {
        Optional<User> existingUser = userRepository.findUserByUsername(user.getUsername());
        User savedUser = null;

        if (existingUser.isPresent()) {
            throw new RuntimeException(MessageResources.USER_ALR_EXIST);
        } else {
            Date date = new Date();
            UUID uuid = UUID.randomUUID();
            user.setId(uuid);
            user.setCreatedDate(date);
            user.setPassword(encryptPassword(user.getPassword()));
            savedUser = userRepository.save(user);
            logger.info("Member has been added: {}", savedUser.toString());
        }

        return savedUser;
    }

    @Override
    public User updateMember(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        User savedUser = null;

        if (existingUser.isPresent()) {
            User currUser = existingUser.get();
            //Librarian only can edit member information
            if (UserRole.MEMBER.equals(currUser.getRole())) {
                Date date = new Date();
                currUser.setRole(user.getRole());
                currUser.setPassword(encryptPassword(user.getPassword()));
                currUser.setStatus(user.getStatus());
                currUser.setUpdatedDate(date);
                savedUser = userRepository.save(currUser);
                logger.info("Member has been updated: {}", savedUser.toString());
            } else if (UserRole.LIBRARIAN.equals(currUser.getRole())) {
                throw new RuntimeException(MessageResources.LIB_CANT_UPDATE_LIB_INFO);
            }
        } else {
            throw new EntityNotFoundException(MessageResources.USER_DOES_NOT_EXIST);
        }

        return savedUser;
    }

    @Override
    public Page<User> getMemberList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllByRole(UserRole.MEMBER, pageable);
    }

    @Override
    public User deleteMember(UUID id) {
        Optional<User> existingUser = userRepository.findById(id);
        User savedUser = null;

        if (existingUser.isPresent()) {
            User currUser = existingUser.get();
            if (UserRole.MEMBER.equals(currUser.getRole())) {
                Date date = new Date();
                currUser.setStatus(UserStatus.DELETED);
                currUser.setUpdatedDate(date);
                savedUser = userRepository.save(currUser);
                logger.info("Member has been set to deleted: {}", currUser.toString());
            } else if (UserRole.LIBRARIAN.equals(currUser.getRole())) {
                throw new RuntimeException(MessageResources.LIB_CANT_DELETE_LIB);
            }
        } else {
            throw new EntityNotFoundException(MessageResources.USER_DOES_NOT_EXIST);
        }

        return savedUser;
    }

    @Override
    public User deleteOwnAccount(User user, String username) {
        Optional<User> existingUser = userRepository.findUserByUsername(username);
        User savedUser = null;

        if (existingUser.isPresent()) {
            User currUser = existingUser.get();
            if (user.getId().equals(currUser.getId())) {
                user.setStatus(UserStatus.DELETED);
                savedUser = userRepository.save(user);
                logger.info("Member's own account has been deleted: {}", user.toString());
            } else {
                throw new RuntimeException(MessageResources.MEMBER_NOT_OWN_ACC);
            }
        } else {
            throw new EntityNotFoundException(MessageResources.ACCOUNT_DONT_EXIST);
        }
        return savedUser;
    }

    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}
