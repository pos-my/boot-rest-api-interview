package posmy.interview.boot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.exception.UserNotFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.RoleRepository;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.system.Constant;
import posmy.interview.boot.system.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private final UserServiceImpl userService = new UserServiceImpl();
    @Spy
    private final UserMapper userMapper = new UserMapper(new ModelMapper());
    @Spy
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BookRepository bookRepository;

    @Test
    void loadUserByUsername_withValidUser_returnUser() {
        String pass = passwordEncoder.encode("password");
        Role librarianRole = Role.builder().id("member").name("MEMBER").build();
        List<Role> roles = Stream.of(librarianRole).collect(Collectors.toList());
        User user = User.builder().id("u1").loginId("user1").name("User 1").pass(pass).build();
        user.setRoles(roles);
        Mockito.doReturn(Optional.of(user)).when(userRepository).findFirstByLoginId("user1");
        UserDetails userDetails = userService.loadUserByUsername("user1");
        assertNotNull(userDetails);
        assertEquals("user1", userDetails.getUsername());
        assertEquals(pass, userDetails.getPassword());
        assertNotNull(userDetails.getAuthorities());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_MEMBER", new ArrayList<>(userDetails.getAuthorities()).get(0).getAuthority());
    }

    @Test
    void loadUserByUsername_withInvalidUser_throwUserNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(userRepository).findFirstByLoginId("user1");
        assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername("user1"));
    }

    @Test
    void findAllMembers_with2Members_return2Members() {
        User member1 = User.builder().id("m1").loginId("member1").build();
        User member2 = User.builder().id("m2").loginId("member2").build();
        List<User> members = Stream.of(member1, member2).collect(Collectors.toList());
        Mockito.doReturn(members).when(userRepository).findByRoleName("MEMBER");
        List<UserDto> memberDtos = userService.findAllMembers();
        assertNotNull(memberDtos);
        assertEquals(2, memberDtos.size());
        List<UserDto> expectedMemberDtos = members.stream().map(userMapper::convertToDto).collect(Collectors.toList());
        assertEquals(expectedMemberDtos, memberDtos);
    }

    @Test
    void findAllMembers_with0Member_return0Member() {
        Mockito.doReturn(new ArrayList<>()).when(userRepository).findByRoleName("MEMBER");
        List<UserDto> memberDtos = userService.findAllMembers();
        assertNotNull(memberDtos);
        assertEquals(0, memberDtos.size());
    }

    @Test
    void createMember_withValidUser_createUserAndReturn() {
        Role memberRole = Role.builder().id("member").name("MEMBER").build();
        Mockito.doReturn(Optional.of(memberRole)).when(roleRepository).findFirstByName("MEMBER");

        User newMember = User.builder().id("m1").loginId("member1").name("Member 1").pass("password")
                .roles(Stream.of(memberRole).collect(Collectors.toList())).build();
        Mockito.doReturn(newMember).when(userRepository).save(ArgumentMatchers.any());
        UserDto newMemberDto = UserDto.builder().id("m1").loginId("member1").name("Member 1").pass("password").build();
        UserDto createdMemberDto = userService.createMember(newMemberDto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, times(1)).save(captor.capture());
        User capturedMember = captor.getValue();
        assertNotNull(capturedMember);
        assertEquals("m1", capturedMember.getId());
        assertEquals("member1", capturedMember.getLoginId());
        assertEquals("Member 1", capturedMember.getName());
        assertTrue(passwordEncoder.matches(newMemberDto.getPass(), capturedMember.getPass()));

        assertEquals("m1", createdMemberDto.getId());
        assertEquals("member1", createdMemberDto.getLoginId());
        assertEquals("Member 1", createdMemberDto.getName());
        assertEquals("MEMBER", createdMemberDto.getRoles().get(0).getName());
    }

    @Test
    void updateMember_withValidMemberAndNameAndPass_updateMemberWithNameAndPassAndReturn() {
        User member = User.builder().id("m1").loginId("member1").name("Old Member 1").pass(passwordEncoder.encode("oldpassword")).build();
        Mockito.doReturn(Optional.of(member)).when(userRepository).findFirstByLoginId("member1");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        User updatedMember = User.builder().id("m1").loginId("member1").name("Member 1").pass("newpassword").build();
        UserDto memberParamDto = UserDto.builder().id("m1").loginId("member1").name("Member 1").pass("newpassword").build();
        Mockito.doReturn(updatedMember).when(userRepository).save(ArgumentMatchers.any());

        userService.updateMember(memberParamDto, "member1");
        Mockito.verify(userRepository, times(1)).save(captor.capture());
        User capturedMember = captor.getValue();
        assertNotNull(capturedMember);
        assertEquals("m1", capturedMember.getId());
        assertEquals("member1", capturedMember.getLoginId());
        assertEquals("Member 1", capturedMember.getName());
        assertFalse(passwordEncoder.matches("oldpassword", capturedMember.getPass()));
        assertTrue(passwordEncoder.matches("newpassword", capturedMember.getPass()));
    }

    @Test
    void updateMember_withValidMemberAndNameOnly_updateMemberWithNameOnlyAndReturn() {
        User member = User.builder().id("m1").loginId("member1").name("Old Member 1").pass(passwordEncoder.encode("oldpassword")).build();
        Mockito.doReturn(Optional.of(member)).when(userRepository).findFirstByLoginId("member1");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        User updatedMember = User.builder().id("m1").loginId("member1").name("Member 1").build();
        UserDto memberParamDto = UserDto.builder().id("m1").loginId("member1").name("Member 1").build();
        Mockito.doReturn(updatedMember).when(userRepository).save(ArgumentMatchers.any());

        userService.updateMember(memberParamDto, "member1");
        Mockito.verify(userRepository, times(1)).save(captor.capture());
        User capturedMember = captor.getValue();
        assertNotNull(capturedMember);
        assertEquals("m1", capturedMember.getId());
        assertEquals("member1", capturedMember.getLoginId());
        assertEquals("Member 1", capturedMember.getName());
        assertTrue(passwordEncoder.matches("oldpassword", capturedMember.getPass()));
    }

    @Test
    void updateMember_withInvalidMember_throwUserNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(userRepository).findFirstByLoginId("m1");
        UserDto userDto = UserDto.builder().build();
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateMember(userDto, "m1");
        });
    }

    @Test
    void deleteMember_withValidMember_deleteMember() {
        Book book1 = Book.builder().id("bk1").name("Book 1").status(Constant.BookState.BORROWED).build();
        Book book2 = Book.builder().id("bk1").name("Book 1").status(Constant.BookState.BORROWED).build();
        List<Book> books = Stream.of(book1, book2).collect(Collectors.toList());

        User member = User.builder().loginId("m1").borrowedBooks(books).build();
        Mockito.doReturn(Optional.of(member)).when(userRepository).findFirstByLoginId("m1");

        ArgumentCaptor<List<Book>> captor = ArgumentCaptor.forClass(ArgumentMatchers.any());

        userService.deleteMember("m1");
        Mockito.verify(bookRepository, times(1)).saveAll(captor.capture());
        List<Book> borrowedBooks = captor.getValue();
        assertTrue(borrowedBooks.stream().allMatch(bb -> bb.getStatus() == Constant.BookState.AVAILABLE));
        Mockito.verify(userRepository, times(1)).delete(member);
    }

    @Test
    void deleteMember_withInvalidMember_throwUserNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(userRepository).findFirstByLoginId("m1");
        assertThrows(UserNotFoundException.class, () -> userService.deleteMember("m1"));
    }
}