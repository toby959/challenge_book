package com.toby959.challenge.repository;

import com.toby959.challenge.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface IAuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books")
    List<Author> findAllAuthorsWithBooks();

    Optional<Author> findAuthorByName(String name);

    @Query("SELECT a FROM Author a WHERE a.birthYear <= :year AND (a.deathYear > :year OR a.deathYear IS NULL)")
    List<Author> findAuthorsWithAge(@Param("year") int year);

    @Query("SELECT a FROM Author a WHERE a.name ILIKE %:nameAuthor%")
    Optional<Author>findAuthors(String nameAuthor);
}
