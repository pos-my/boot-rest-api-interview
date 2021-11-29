package posmy.interview.boot.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import posmy.interview.boot.constant.Constant;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.repo.BookRepository;
import posmy.interview.boot.repo.UserRepository;
import posmy.interview.boot.service.UserService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            log.error(Constant.USER_ERROR_USER_NOT_EXIST);
            throw new RuntimeException(Constant.USER_ERROR_USER_NOT_EXIST);
        }

        if (Constant.UserStatus.INACTIVE.name().equals(user.get().getStatus())) {
            log.error(Constant.USER_ERROR_USER_IS_INACTIVE);
            throw new RuntimeException(Constant.USER_ERROR_USER_IS_INACTIVE);
        }
        Collection<SimpleGrantedAuthority> authority = List.of(new SimpleGrantedAuthority(user.get().getRole()));
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), authority);
    }

    @Override
    public User addUser(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            log.error(Constant.USER_ERROR_USER_EXIST);
            throw new EntityExistsException(Constant.USER_ERROR_USER_EXIST);
        }

        Date date = new Date();
        user.setStatus(Constant.UserStatus.ACTIVE.name());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDateCreated(date);
        user.setDateUpdated(date);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        Optional<User> optionalUser = userRepository.findByUsernameAndStatus(user.getUsername(), Constant.UserStatus.ACTIVE.name());
        if (!optionalUser.isPresent()) {
            log.error(Constant.USER_ERROR_USER_NOT_EXIST);
            throw new EntityNotFoundException(Constant.USER_ERROR_USER_NOT_EXIST);
        }

        User existingUser = optionalUser.get();
        existingUser.setDateUpdated(new Date());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

    @Override
    public User deleteUser(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        Optional<Book> optionalBook = bookRepository.findByBorrower(user.getUsername());

        if (!optionalUser.isPresent()) {
            log.error(Constant.USER_ERROR_USER_NOT_EXIST);
            throw new EntityNotFoundException(Constant.USER_ERROR_USER_NOT_EXIST);
        }

        if (optionalBook.isPresent()) {
            log.error(Constant.USER_ERROR_HAS_BOOK);
            throw new RuntimeException(Constant.USER_ERROR_HAS_BOOK);
        }

        User existingUser = optionalUser.get();
        if (Constant.UserRole.LIBRARIAN.name().equals(existingUser.getRole())) {
            log.error(Constant.USER_ERROR_LIB_NOT_ALL_DEL);
            throw new RuntimeException(Constant.USER_ERROR_LIB_NOT_ALL_DEL);
        }

        if (Constant.UserStatus.INACTIVE.name().equals(existingUser.getStatus())) {
            log.error(Constant.USER_ERROR_USER_IS_INACTIVE);
            throw new RuntimeException(Constant.USER_ERROR_USER_IS_INACTIVE);
        }
        existingUser.setDateUpdated(new Date());
        existingUser.setStatus(Constant.UserStatus.INACTIVE.name());
        return userRepository.save(existingUser);
    }

    @Override
    public Page<User> getMembers(int page) {
        if (Constant.MIN_PAGE > page) {
            log.error(Constant.PAGE_ERROR);
            throw new RuntimeException(Constant.PAGE_ERROR);
        }

        Page<User> members = userRepository.findAllByRole(Constant.UserRole.MEMBER.name(), PageRequest.of(page, Constant.PAGE_INDEX));
        return members;
    }

    @Override
    public User getMemberByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            log.error(Constant.USER_ERROR_USER_NOT_EXIST);
            throw new EntityNotFoundException(Constant.USER_ERROR_USER_NOT_EXIST);
        }
        return optionalUser.get();
    }

    @Override
    public User deleteOwnAccount(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            log.error(Constant.USER_ERROR_USER_NOT_EXIST);
            throw new EntityNotFoundException(Constant.USER_ERROR_USER_NOT_EXIST);
        }

        User existingUser = optionalUser.get();
        existingUser.setDateUpdated(new Date());
        existingUser.setStatus(Constant.UserStatus.INACTIVE.name());
        return userRepository.save(existingUser);
    }

}
