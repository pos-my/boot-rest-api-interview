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
 * @version $Id: BaseApiResponse.java, v 0.1 2021-05-24 12:39 PM Bennett Exp $$
 */
@Getter
@Setter
public class BaseApiResponse {

    private String message;

    private BaseResult result;
}