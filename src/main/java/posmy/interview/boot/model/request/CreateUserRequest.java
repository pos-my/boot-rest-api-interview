/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.request;

import lombok.Getter;
import lombok.Setter;
import posmy.interview.boot.model.entity.Role;

import java.util.List;

/**
 * @author Bennett
 * @version $Id: CreateUserRequest.java, v 0.1 2021-05-24 1:28 PM Bennett Exp $$
 */
@Getter
@Setter
public class CreateUserRequest {

    private String username;

    private String password;

    private List<Role> roles;
}