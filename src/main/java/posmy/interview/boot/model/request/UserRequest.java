/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Bennett
 * @version $Id: UserRequest.java, v 0.1 2021-05-24 1:28 PM Bennett Exp $$
 */
@Getter
@Setter
public class UserRequest {

    private String username;

    private String password;

    private String name;

    private String mobileNo;

    private List<String> roles;
}