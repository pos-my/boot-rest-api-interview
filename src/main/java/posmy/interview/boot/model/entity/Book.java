/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.entity;

import javax.persistence.*;

/**
 * @author Bennett
 * @version $Id: Book.java, v 0.1 2021-05-24 12:15 PM Bennett Exp $$
 */
@Entity
@Table(name = "book_table")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "user_borrow")
    private String userBorrow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getUserBorrow() {
        return userBorrow;
    }

    public void setUserBorrow(String userBorrow) {
        this.userBorrow = userBorrow;
    }
}