/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Bennett
 * @version $Id: BookStatusEnum.java, v 0.1 2021-05-24 12:33 PM Bennett Exp $$
 */
@Getter
public enum BookStatusEnum {
    /***/
    AVAILABLE("AVAILABLE","available status"),
    BORROWED("BORROWED","borrow status");

    /**
     * code for status
     */
    private String code;

    /**
     * description for status
     */
    private String description;

    /**
     *
     * @param code String
     * @param description String
     */
    BookStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}