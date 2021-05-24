/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.apiresponse;

import lombok.Getter;
import lombok.Setter;
import posmy.interview.boot.model.result.BaseResult;

/**
 * @author Bennett
 * @version $Id: UserApiResponse.java, v 0.1 2021-05-24 12:39 PM Bennett Exp $$
 */
@Getter
@Setter
public class UserApiResponse {

    private String username;

    private String message;

    private boolean success;

    private BaseResult result;
}