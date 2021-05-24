/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.model.entity.Book;

import java.util.Optional;

/**
 * @author Bennett
 * @version $Id: BookRepository.java, v 0.1 2021-05-24 12:17 PM Bennett.hds Exp $$
 */
@Repository
public interface BookRepository extends JpaRepository<Book, String > {

    Optional<Book> findByBookName(String bookName);
}