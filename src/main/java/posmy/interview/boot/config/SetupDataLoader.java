package posmy.interview.boot.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import posmy.interview.boot.entities.Privilege;
import posmy.interview.boot.entities.PrivilegeRepository;
import posmy.interview.boot.entities.Role;
import posmy.interview.boot.entities.RoleRepository;
import posmy.interview.boot.entities.User;
import posmy.interview.boot.entities.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup) {
			return;
		}
		Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

		List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
		createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

		Role adminRole = roleRepository.findByName("ROLE_ADMIN");
		User librarian = new User();
		librarian.setFirstName("Librarian");
		librarian.setLastName("Librarian");
		librarian.setPassword(passwordEncoder.encode("librarian"));
		librarian.setEmail("librarian@librarian.com");
		librarian.setRoles(Arrays.asList(adminRole));
		librarian.setEnabled(true);
		userRepository.saveAndFlush(librarian);
		
		User member = new User();
		member.setFirstName("Member");
		member.setLastName("Member");
		member.setPassword(passwordEncoder.encode("member"));
		member.setEmail("member@member.com");
		member.setRoles(Arrays.asList(adminRole));
		member.setEnabled(true);
		userRepository.saveAndFlush(member);

		alreadySetup = true;
	}

	@Transactional
	Privilege createPrivilegeIfNotFound(String name) {

		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			roleRepository.save(role);
		}
		return role;
	}
}
