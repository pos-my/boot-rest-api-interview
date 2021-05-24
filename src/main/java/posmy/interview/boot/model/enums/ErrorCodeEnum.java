/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.enums;

import lombok.Getter;

/**
 * @author Bennett
 * @version $Id: ErrorCodeEnum.java, v 0.1 2021-05-24 5:18 PM Bennett Exp $$
 */
@Getter
public enum ErrorCodeEnum {
    /** available status*/
    IDEMPOTENT_USER("IDEMPOTENT_USER", "User is already created."),

    USER_NOT_FOUND("USER_NOT_FOUND", "user not found in system"),

    BOOK_NOT_FOUND("BOOK_NOT_FOUND", "book not found in system"),

    BOOK_NOT_AVAILABLE("BOOK_NOT_AVAILABLE", "book not available"),

    BOOK_EXIST("BOOK_EXIST", "book already exist"),

    BOOK_ALREADY_RETURN("BOOK_ALREADY_RETURN","book already returned")
    ;


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
    ErrorCodeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}