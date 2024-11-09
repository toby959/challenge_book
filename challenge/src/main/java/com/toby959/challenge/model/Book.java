package com.toby959.challenge.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Language language;

    private int downloadCount;

    @ManyToMany(fetch = FetchType.EAGER)//, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"),
    inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    public Book() {}

    public Book( DataBook book) {
        this.title = book.title();
        this.downloadCount = book.downloadCount();
        if (!book.languages().isEmpty()) {
            this.language = Language.fromString(book.languages().get(0));
        } else {
            throw new IllegalArgumentException("No se proporciono un idioma válido!!!");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        String authorNames = authors.stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));

        return String.format("Libro: \"%s\"\nIdioma: %s\nDescargas: %d\nAutores: %s",
                title, language, downloadCount, authorNames);
    }

    // Métodos para agregar y eliminar autores
    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this); // Asegúrate de mantener la relación bidireccional
    }

    public void removeAuthor(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this); // Mantener la relación bidireccional
    }
}
