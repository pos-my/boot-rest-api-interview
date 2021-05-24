/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.result;

import lombok.Getter;
import lombok.Setter;
import posmy.interview.boot.model.entity.User;

import java.util.List;

/**
 * @author Bennett
 * @version $Id: UserQueryResult.java, v 0.1 2021-05-24 6:52 PM Bennett Exp $$
 */
@Getter
@Setter
public class UserQueryResult extends UserServiceResult {
    private List<User> userList;
}