package posmy.interview.boot.UnitTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import posmy.interview.boot.domain.User;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserStatus;
import posmy.interview.boot.manager.MessageResources;
import posmy.interview.boot.manager.UserManager;
import posmy.interview.boot.repo.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class UserManagerImplTest {
    @Autowired
    private UserManager userManager;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testAddMember() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("tester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User addedUser = userManager.addMember(user);

        assertThat(user.equals(addedUser)).isTrue();
    }

    @Test
    public void testAddMember_UserAlreadyExist() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("tester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        RuntimeException re = assertThrows(RuntimeException.class, () -> userManager.addMember(user));

        assertTrue(re.getMessage().contains(MessageResources.USER_ALR_EXIST));
    }

    @Test
    public void testUpdateMember() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("tester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();
        User updatedUser = User.builder().id(userId).username("tester").password("p@ssw0rd").role(UserRole.LIBRARIAN).status(UserStatus.ACTIVE).build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User savedUser = userManager.updateMember(user);

        assertThat(savedUser.getRole()).isNotEqualTo(user.getRole());
    }

    @Test
    public void testUpdateMember_CannotUpdateLibrarian() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("tester").password("p@ssw0rd").role(UserRole.LIBRARIAN).status(UserStatus.ACTIVE).build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        RuntimeException re = assertThrows(RuntimeException.class, () -> userManager.updateMember(user));

        assertTrue(re.getMessage().contains(MessageResources.LIB_CANT_UPDATE_LIB_INFO));
    }

    @Test
    public void testUpdateMember_UserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("tester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException re = assertThrows(EntityNotFoundException.class, () -> userManager.updateMember(user));

        assertTrue(re.getMessage().contains(MessageResources.USER_DOES_NOT_EXIST));
    }

    @Test
    public void testGetMemberList() {
        User user = User.builder().id(UUID.randomUUID()).username("tester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        User user1 = User.builder().id(UUID.randomUUID()).username("tester1").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);

        Page<User> pageUser = new PageImpl<>(userList);

        when(userRepository.findAllByRole(any(UserRole.class), any(Pageable.class))).thenReturn(pageUser);

        Page<User> resultPage = userManager.getMemberList(0, 1);

        assertTrue(resultPage.getContent().containsAll(userList));
    }

    @Test
    public void testDeleteMember() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("tester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userManager.deleteMember(userId);

        assertThat(savedUser.getStatus()).isNotEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void testDeleteMember_CannotDeleteLibrarian() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("tester").password("p@ssw0rd").role(UserRole.LIBRARIAN).status(UserStatus.ACTIVE).build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        RuntimeException re = assertThrows(RuntimeException.class, () -> userManager.deleteMember(userId));

        assertTrue(re.getMessage().contains(MessageResources.LIB_CANT_DELETE_LIB));
    }

    @Test
    public void testDeleteMember_UserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException re = assertThrows(EntityNotFoundException.class, () -> userManager.deleteMember(userId));

        assertTrue(re.getMessage().contains(MessageResources.USER_DOES_NOT_EXIST));
    }

    @Test
    public void testDeleteOwnAccount() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("tester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userManager.deleteOwnAccount(user, user.getUsername());

        assertThat(savedUser.getStatus()).isNotEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void testDeleteOwnAccount_MemberNotOwnAccount() {
        User user = User.builder().id(UUID.randomUUID()).username("tester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        User user1 = User.builder().id(UUID.randomUUID()).username("nottester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user1));

        RuntimeException re = assertThrows(RuntimeException.class, () -> userManager.deleteOwnAccount(user, user1.getUsername()));

        assertTrue(re.getMessage().contains(MessageResources.MEMBER_NOT_OWN_ACC));
    }

    @Test
    public void testDeleteOwnAccount_AccountDoesNotExist() {
        User user = User.builder().id(UUID.randomUUID()).username("tester").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException re = assertThrows(EntityNotFoundException.class, () -> userManager.deleteOwnAccount(user, user.getUsername()));

        assertTrue(re.getMessage().contains(MessageResources.ACCOUNT_DONT_EXIST));
    }
}
