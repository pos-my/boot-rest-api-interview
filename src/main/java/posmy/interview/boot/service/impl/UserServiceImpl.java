/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.service.impl;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.entity.Role;
import posmy.interview.boot.model.entity.User;
import posmy.interview.boot.model.enums.ErrorCodeEnum;
import posmy.interview.boot.model.request.UserRequest;
import posmy.interview.boot.model.result.UserQueryResult;
import posmy.interview.boot.model.result.UserServiceResult;
import posmy.interview.boot.repository.RoleRepository;
import posmy.interview.boot.repository.UserRespository;
import posmy.interview.boot.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Bennett
 * @version $Id: UserServiceImpl.java, v 0.1 2021-05-24 12:20 PM Bennett Exp $$
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserServiceResult createUser(UserRequest request){
        UserServiceResult result = new UserServiceResult();
        Optional<User> user = userRespository.findByUsername(request.getUsername());
        if(user.isPresent()){
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.IDEMPOTENT_USER.getCode());
            result.setErrorDesc(ErrorCodeEnum.IDEMPOTENT_USER.getDescription());
            result.setUsername(request.getUsername());
        }
        else {
            User newUser = compileUser(request);
            userRespository.save(newUser);

            result.setSuccess(true);
            result.setUsername(request.getUsername());
        }
        return result;
    }

    @Override
    public UserServiceResult updateUser(UserRequest request){
        UserServiceResult result = new UserServiceResult();
        Optional<User> user = userRespository.findByUsername(request.getUsername());
        if(!user.isPresent()){
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.USER_NOT_FOUND.getCode());
            result.setErrorDesc(ErrorCodeEnum.USER_NOT_FOUND.getDescription());
            result.setUsername(request.getUsername());
        } else {
            User updateUser = user.get();
            updateUser.setUsername(request.getUsername());
            updateUser.setName(request.getName());
            updateUser.setMobileNo(request.getMobileNo());
            updateUser.setPassword(passwordEncoder.encode(request.getPassword()));
            updateUser.setRoles(buildUserRole(request.getRoles()));
            userRespository.save(updateUser);

            result.setSuccess(true);
            result.setUsername(request.getUsername());
        }
        return result;
    }

    @Override
    public UserQueryResult getUserByUserName(String username) {
        UserQueryResult result = new UserQueryResult();
        Optional<User> user = userRespository.findByUsername(username);
        if(!user.isPresent()) {
            result.setSuccess(false);
            result.setErrorContext(ErrorCodeEnum.USER_NOT_FOUND.getCode());
            result.setErrorDesc(ErrorCodeEnum.USER_NOT_FOUND.getDescription());
            result.setUsername(username);
        } else {
            List<User> userQueryList = new ArrayList<>();
            userQueryList.add(user.get());
            result.setUserList(userQueryList);
            result.setUsername(user.get().getUsername());
            result.setSuccess(true);
        }
        return result;
    }

    @Override
    public UserQueryResult getAllUsers() {
        UserQueryResult result = new UserQueryResult();
        List<User> userQueryList = userRespository.findAll();
        result.setSuccess(true);
        result.setUserList(userQueryList);
        return result;
    }

    @Override
    public UserServiceResult deleteUserByUserName(String username){
        UserServiceResult result = new UserServiceResult();
        Optional<User> user = userRespository.findByUsername(username);
        if(user.isPresent()){
            userRespository.delete(user.get());
        }
        result.setSuccess(true);
        result.setUsername(username);
        return result;
    }

    private List<Role> buildUserRole(List<String> list) {
        List<Role> roleList = new ArrayList<>();

        for(String roleName: list) {
            if(null == findRoleByName(roleName)) {
                throw new NullPointerException();
            }
            roleList.add(findRoleByName(roleName));

        }
        return roleList;
    }

    private Role findRoleByName(String roleName) {
        try {
            return roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new NotFoundException("Role not found: "+ roleName));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User compileUser(UserRequest request){
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setName(request.getName());
        newUser.setMobileNo(request.getMobileNo());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(buildUserRole(request.getRoles()));

        return newUser;
    }

}