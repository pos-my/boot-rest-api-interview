package posmy.interview.boot.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.exception.BadRequestException;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.UserRepo;
import posmy.interview.boot.service.api.RoleService;
import posmy.interview.boot.service.api.UserService;
import posmy.interview.boot.snowflake.SnowflakeHelper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;


    @Override
    public List<User> saveAll(List<User> users) {
        //todo: some validation can do such as not allow empty list
        // roles cannot be empty
        // , using validator to validate the field using @NotNull etc in future
        log.info("save users: {}", users);
        //set id when it is new role insert
        SnowflakeHelper.assignLongIds(users);
        //encode password by encoder and assign role by roles name
        encodePwdAssignRoles(users);
        //save and return the saved data
        return userRepo.saveAll(users);
    }

    @Override
    public User findByUsername(String username) {
        log.info("get user: {}", username);
        //find the user, if not found throw error
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not exists"));
    }

    @Override
    public void remove(String username) {
        log.info("remove user: {}", username);
        userRepo.delete(this.findByUsername(username));
    }

    @Override
    public void removeMe() {
        var username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("remove current user: {}", username);
        //remove me by get the user name from security context
        this.remove(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("load user for auth: {}", username);
        User user = this.findByUsername(username);
        //convert the role to standard authority
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


    private void encodePwdAssignRoles(List<User> users) {
        //todo: add cache for role service all retrieve to increase performance in future
        //todo: add stomp cache and trigger cache refresh when add/edit/delete role happen in future

        var roles = roleService.findAll();
        for (User user : users) {
            //encode pwd
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            for (Role role : user.getRoles()) {
                //assign role if the role id is null
                // consider front end will assign correct role id which probably they will retrieve the roles list and assign
                if (role.getId() == null) {
                    role.setId(roles.stream().filter(r -> r.getName().equals(role.getName())).findFirst()
                            .orElseThrow(() -> new BadRequestException(String.format("Role not exists: %s", role.getName()))).getId());
                }
            }
        }
    }
}
