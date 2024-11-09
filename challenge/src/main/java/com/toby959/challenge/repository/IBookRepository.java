package com.toby959.challenge.repository;

import com.toby959.challenge.model.Book;
import com.toby959.challenge.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IBookRepository extends JpaRepository<Book, Long> {

    List<Book> findByLanguage(Language language);

//    List<Book> findTop10By(Pageable pageable);

    List<Book> findTop10ByOrderByDownloadCountDesc();

    Optional<Object> findByTitle(String title);
}
