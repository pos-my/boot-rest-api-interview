package posmy.interview.boot.service;

import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.repository.RoleRepository;
import posmy.interview.boot.testutils.factories.RoleFactory;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Should get a list of roles")
    void getRoles() {
        List<Role> mockRoles = RoleFactory.getInstance().constructListOfRole();

        when(roleRepository.findAll()).thenReturn(mockRoles);

        List<Role> roles = roleService.getRoles();

        Assertions.assertEquals(roles.size(), mockRoles.size());
        Assertions.assertEquals(roles.get(0).getId(), mockRoles.get(0).getId());
    }
}
