/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.service;

import posmy.interview.boot.model.request.UserRequest;
import posmy.interview.boot.model.result.UserQueryResult;
import posmy.interview.boot.model.result.UserServiceResult;

/**
 * @author Bennett
 * @version $Id: UserServiceImpl.java, v 0.1 2021-05-24 11:31 PM Bennett.hds Exp $$
 */
public interface UserService {
    UserServiceResult createUser(UserRequest request);

    UserServiceResult updateUser(UserRequest request);

    UserQueryResult getUserByUserName(String username);

    UserQueryResult getAllUsers();

    UserServiceResult deleteUserByUserName(String username);

}