package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.model.AppUser;
import posmy.interview.boot.model.AppUserRole;
import posmy.interview.boot.repo.AppUserRepo;
import posmy.interview.boot.repo.AppUserRoleRepo;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {
    @Mock
    private AppUserRepo userRepo;
    @Mock
    private AppUserRoleRepo roleRepo;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AppUserServiceImpl serviceUnderTest;

    @BeforeEach
    void setUp(){
        serviceUnderTest = new AppUserServiceImpl(userRepo, roleRepo, passwordEncoder);
    }

    @Test
    void canLoadSpringSecurityUserByUsername(){
        //given
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(new AppUserRole(1L, "MEMBER"));
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, roles);
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        //when
        UserDetails springSecurityUserResult = serviceUnderTest.loadUserByUsername(user.getUsername());

        //then
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));
        UserDetails expectedSpringSecurityUser = new User(user.getUsername(), user.getPassword(), authorities);
        assertThat(springSecurityUserResult)
                .isEqualTo(expectedSpringSecurityUser);
    }

    @Test
    void canAddUser() throws ValidationException {
        //given
        AppUser userWithNoId = new AppUser(null, "Cheah ZZ", "zz", "1234", 24, new ArrayList<>());
        given(passwordEncoder.encode(userWithNoId.getPassword()))
                .willReturn("dummy_encoded_password");

        //when
        serviceUnderTest.saveUser(userWithNoId);

        //then
        verify(userRepo).save(userWithNoId);
    }

    @Test
    void canUpdateUser() throws ValidationException {
        //given
        AppUser userWithId = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, new ArrayList<>());
        given(userRepo.findById(userWithId.getId()))
                .willReturn(Optional.of(userWithId));
        given(passwordEncoder.encode(userWithId.getPassword()))
                .willReturn("dummy_encoded_password");

        //when
        serviceUnderTest.saveUser(userWithId);

        //then
        verify(userRepo).findById(userWithId.getId());
        verify(userRepo).save(userWithId);
    }

    @Test
    void willThrowWhenUpdateUserWithLibrarianRole() {
        //given
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(new AppUserRole(1L, "LIBRARIAN"));
        AppUser userWithLibrarianRole = new AppUser(1L, "Sean", "sean", "1234", 24, roles);
        given(userRepo.findById(userWithLibrarianRole.getId()))
                .willReturn(Optional.of(userWithLibrarianRole));

        //when
        //then
        assertThatThrownBy(() -> serviceUnderTest.saveUser(userWithLibrarianRole))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Unable to update user with role LIBRARIAN");
    }

    @Test
    void canSaveUserRole() {
        //given
        AppUserRole role = new AppUserRole(null, "MEMBER");

        //when
        serviceUnderTest.saveUserRole(role);

        //then
        verify(roleRepo).save(role);
    }

    @Test
    void canAddRoleToUser() throws ValidationException {
        //given
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, new ArrayList<>());
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));
        AppUserRole role = new AppUserRole(null, "MEMBER");
        given(roleRepo.findByName(role.getName()))
                .willReturn(Optional.of(role));

        //when
        boolean result = serviceUnderTest.addRoleToUser(user.getUsername(), role.getName());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void willThrowIfRoleAlreadyExistWhileAddingRoleToUser() {
        //given
        AppUserRole role = new AppUserRole(null, "MEMBER");
        given(roleRepo.findByName(role.getName()))
                .willReturn(Optional.of(role));
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(role);
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, roles);
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> serviceUnderTest.addRoleToUser(user.getUsername(), role.getName()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Role")
                .hasMessageContaining("already added to user");
    }

    @Test
    void canRemoveRoleFromUser() throws ValidationException {
        //given
        AppUserRole role = new AppUserRole(null, "MEMBER");
        given(roleRepo.findByName(role.getName()))
                .willReturn(Optional.of(role));
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(role);
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, roles);
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        //when
        boolean result = serviceUnderTest.removeRoleFromUser(user.getUsername(), role.getName());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void willThrowIfRoleNotExistsWhileRemoveRoleFromUser(){
        //given
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, new ArrayList<>());
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));
        AppUserRole role = new AppUserRole(null, "MEMBER");
        given(roleRepo.findByName(role.getName()))
                .willReturn(Optional.of(role));

        //when
        //then
        assertThatThrownBy(() -> serviceUnderTest.removeRoleFromUser(user.getUsername(), role.getName()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("not having role");
    }

    @Test
    void canGetUser() {
        //given
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, new ArrayList<>());
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        //when
        serviceUnderTest.getUser(user.getUsername());

        //then
        verify(userRepo).findByUsername(user.getUsername());
    }

    @Test
    void canGetAllUsers() {
        //when
        serviceUnderTest.getUsers();

        //then
        verify(userRepo).findAll();
    }

    @Test
    void canRemoveUser() {
        //given
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, new ArrayList<>());
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        //when
        serviceUnderTest.removeUser(user.getUsername());

        //then
        verify(userRepo).delete(user);
    }
}