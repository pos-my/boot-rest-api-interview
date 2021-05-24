/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.entity.Role;
import posmy.interview.boot.model.entity.User;
import posmy.interview.boot.model.request.CreateUserRequest;
import posmy.interview.boot.model.result.CreateUserResult;
import posmy.interview.boot.repository.RoleRepository;
import posmy.interview.boot.repository.UserRespository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Bennett
 * @version $Id: UserService.java, v 0.1 2021-05-24 12:20 PM Bennett Exp $$
 */
@Service
public class UserService {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CreateUserResult createUser(CreateUserRequest request){
        CreateUserResult result = new CreateUserResult();
        Optional<User> user = userRespository.findByUsername(request.getUsername());
        if(user.isPresent()){
            result.setSuccess(false);
            result.setErrorContext("");
            result.setErrorDesc("");
            result.setUsername(request.getUsername());
        }
        else {
            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRoles(buildUserRole(request.getRoles()));

            userRespository.save(newUser);

            result.setSuccess(true);
            result.setUsername(request.getUsername());
        }
        return result;
    }

    private List<Role> buildUserRole(List<String> list) {
        List<Role> roleList = new ArrayList<>();

        for(String roleName: list) {
            roleList.add(roleRepository.findByRoleName(roleName));
        }
        return roleList;
    }
}