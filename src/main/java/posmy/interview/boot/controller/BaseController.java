/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.controller;

import posmy.interview.boot.model.apiresponse.UserApiResponse;
import posmy.interview.boot.model.result.BaseResult;

/**
 * @author Bennett
 * @version $Id: BaseController.java, v 0.1 2021-05-24 5:01 PM Bennett Exp $$
 */
public class BaseController {

    protected UserApiResponse composeUserApiSuccessResponse(String username, String message){
        UserApiResponse response = new UserApiResponse();
        response.setUsername(username);
        response.setSuccess(true);
        response.setMessage(message);
        return response;
    }

    protected UserApiResponse composeUserApiFailedResponse(String username,String message,BaseResult result) {
        UserApiResponse response = new UserApiResponse();
        response.setUsername(username);
        response.setMessage(message);
        response.setResult(result);
        return response;
    }
}