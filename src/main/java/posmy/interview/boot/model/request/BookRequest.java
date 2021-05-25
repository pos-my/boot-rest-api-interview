/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Bennett
 * @version $Id: BookRequest.java, v 0.1 2021-05-24 7:37 PM Bennett Exp $$
 */
@Getter
@Setter
public class BookRequest {

    private String bookTitle;

    private String author;

    private String status;

    private String username;

}