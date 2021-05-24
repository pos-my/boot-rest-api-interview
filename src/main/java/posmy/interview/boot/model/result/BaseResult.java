/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Bennett
 * @version $Id: BaseResult.java, v 0.1 2021-05-24 1:34 PM Bennett Exp $$
 */
@Getter
@Setter
public class BaseResult {

    private boolean success;

    private String errorContext;

    private String errorDesc;
}