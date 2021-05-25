/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Bennett
 * @version $Id: Book.java, v 0.1 2021-05-24 12:15 PM Bennett Exp $$
 */
@Entity
@Table(name = "book_table")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "author")
    private String author;

    @Column(name = "username")
    private String username;

    @Column(name = "book_status")
    private String bookStatus;
}