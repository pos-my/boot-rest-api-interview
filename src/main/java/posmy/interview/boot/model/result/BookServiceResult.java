/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Bennett
 * @version $Id: BookServiceResult.java, v 0.1 2021-05-24 7:41 PM Bennett Exp $$
 */
@Getter
@Setter
public class BookServiceResult extends BaseResult{

    private String bookTitle;

    private String borrowedUser;
}