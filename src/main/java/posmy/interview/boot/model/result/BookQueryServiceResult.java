/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.result;

import lombok.Getter;
import lombok.Setter;
import posmy.interview.boot.model.entity.Book;

import java.util.List;

/**
 * @author Bennett
 * @version $Id: BookQueryServiceResult.java, v 0.1 2021-05-24 9:06 PM Bennett Exp $$
 */
@Getter
@Setter
public class BookQueryServiceResult extends BaseResult{
    private List<Book> bookList;
}