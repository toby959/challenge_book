package com.toby959.challenge.main;

import com.toby959.challenge.model.*;
import com.toby959.challenge.repository.IAuthorRepository;
import com.toby959.challenge.repository.IBookRepository;
import com.toby959.challenge.service.ConsumeAPI;
import com.toby959.challenge.service.ConvertData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Main {
    private final Scanner search = new Scanner(System.in);
    private final ConsumeAPI consumeAPI;
    private final ConvertData convertData;
    private final IBookRepository bookRepository;
    private final IAuthorRepository authorRepository;
    private List<Book> books;
    private List<Author> authors;

    private final String URL_BASE = "https://gutendex.com/books?search=";

    @Autowired
    public Main(ConsumeAPI consumeAPI, ConvertData convertData, IBookRepository bookRepository, IAuthorRepository authorRepository) {
        this.consumeAPI = consumeAPI;
        this.convertData = convertData;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showTheMenu() {
        boolean continuar = true;

        while (continuar) {
            showMenu();
            int option = getOptionUser();

            switch (option) {
                case 1:
                    searchBook();
                    break;
                case 2:
                    listBooks();
                    break;
                case 3:
                    searchLanguage();
                    break;
                case 4:
                    listAuthors();
                    break;
                case 5:
                    searchData();
                    break;
                case 6:
                    listTop10();
                    break;
                case 7:
                    searchAuthor();
                    break;
                case 8:
                    showDownloadStatistics();
                    break;
                case 9:
                    continuar = false;
                    System.out.println("Saliendo del programa. ¡Hasta pronto!!!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción correcta.");
            }
        }
    }

    private void showMenu() {
        System.out.println("#######################################################");
        System.out.println("\uD83D\uDCDA ¡Bienvenido a la consulta de libros! \uD83D\uDCDA");
        System.out.println("Seleccione una opción:");
        System.out.println("1. Buscar un libro por nombre");
        System.out.println("2. Listar Libros Registrados");
        System.out.println("3. Listar Libros por idioma");
        System.out.println("4. Listar Autores registrados");
        System.out.println("5. Listar Autores según año");
        System.out.println("6. Ver los 10 libros más descargados");
        System.out.println("7. Buscar Autores por nombre");
        System.out.println("8. Ver estadísticas de descargas");
        System.out.println("9. Salir");
        System.out.println("#######################################################");
    }
// one < -- >
    private int getOptionUser() {
        int option = -1;
        boolean opcionValida = false;

        while (!opcionValida) {
            try {
                option = Integer.parseInt(search.nextLine().trim());
                if (option >= 1 && option <= 9) {
                    opcionValida = true;
                } else {
                    System.out.println("Por favor, seleccione una opción entre 1 y 9.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
            }
        }
        return option;
    }


    private DataBook dataUser() {
        System.out.print("Ingresa el título del libro que deseas buscar: ");
        var title = search.nextLine();
                                //obtenerDatos
        String json = consumeAPI.getData(URL_BASE + title.toLowerCase().replace(" ", "+"));
                                     //obtenerDatos
        Data dataResult = convertData.getData(json, Data.class);
        return dataResult.results().stream()
                .filter(b -> b.title().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    @Transactional
    private void searchBook() {
        try {
            DataBook dataBook = dataUser();
            if (dataBook != null) {
                Book book = new Book(dataBook);
                for (DataAuthor dataAuthor : dataBook.authors()) {
                    Author author;
                    Optional<Author> existingAuthor = authorRepository.findAuthorByName(dataAuthor.name());
                    if (existingAuthor.isPresent()) {
                        author = existingAuthor.get(); // Autor existente
                    } else {
                        author = new Author(dataAuthor);
                        authorRepository.save(author);  // Guardar el nuevo autor
                    }
                    book.addAuthor(author);  // Agregar el autor al libro
                }
                bookRepository.save(book); // Guarda el libro
                System.out.println("Libro guardado exitosamente:\n" + formatBook(book));
            } else {
                System.out.println("Libro no encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar o guardar el libro: " + e.getMessage());
        }
    }



// two < -- >
    private void listBooks() {
        books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            books.stream()
                    .sorted(Comparator.comparing(Book::getLanguage))
                    .forEach(book -> System.out.println(formatBook(book)));
        }
    }
// tree < -- >
private void searchLanguage() {
    System.out.print("Ingresa el idioma que deseas filtrar (ejemplo: es | en | fr | pt | la | de | it): ");
    System.out.println();

    String idioma = search.nextLine().trim();

    Language language;
    try {
        language = Language.fromString(idioma.toLowerCase());

        if (language == null) {
            System.out.println("El idioma ingresado no es válido. Por favor, intenta de nuevo.");
            return;
        }

        List<Book> idiomBooks = bookRepository.findByLanguage(language);
        showBooksByLanguage(language, idiomBooks); // Cambiar a pasar el objeto Language

    } catch (IllegalArgumentException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    private void showBooksByLanguage(Language language, List<Book> idiomBooks) {
        String languageName = getLanguageName(language); // Obtener el nombre completo del idioma
        System.out.println("Libros en " + languageName + ":");

        if (idiomBooks.isEmpty()) {
            System.out.println("No se encontraron libros en " + languageName + ".");
        } else {
            idiomBooks.forEach(book -> System.out.println(formatBook(book)));
        }
    }

    private String getLanguageName(Language language) {
        Map<Language, String> languageNames = new HashMap<>();

        // Mapeo de idiomas a sus nombres completos
        languageNames.put(Language.ESPAÑOL, "español");
        languageNames.put(Language.INGLES, "inglés");
        languageNames.put(Language.FRANCES, "francés");
        languageNames.put(Language.PORTUGUES, "portugués");
        languageNames.put(Language.LATIN, "latín");
        languageNames.put(Language.ALEMAN, "alemán");
        languageNames.put(Language.ITALIANO, "italiano");

        return languageNames.getOrDefault(language, "idioma desconocido"); // Devuelve un nombre por defecto si no se encuentra
    }

// four < -- >
    private void listAuthors() {
        authors = authorRepository.findAllAuthorsWithBooks();
        if (authors.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            authors.stream()
                    .sorted(Comparator.comparing(Author::getName))
                    .forEach(author -> System.out.println(formatAuthor(author)));
        }
    }
// five < -- >
    private void searchData() {
        int year = getUserYear();
        if (year < 0) {
            System.out.println("Año no válido. Por favor, ingresa un año positivo.");
            return;
        }

        List<Author> authorsByYear = authorRepository.findAuthorsWithAge(year);
        showAuthorsByYear(authorsByYear);
    }

    private int getUserYear() {
        System.out.print("Ingresa el año a buscar (debe tener 4 dígitos): ");
        String input;
        int year = -1; // Valor por defecto para indicar un año no válido

        while (true) {
            input = search.nextLine().trim(); // Captura la entrada del usuario

            // Verifica que la entrada tenga exactamente 4 dígitos y sea un número
            if (input.matches("\\d{4}")) {
                year = Integer.parseInt(input);
                break;
            } else {
                System.out.println("Entrada no válida. Por favor, ingresa un año de 4 dígitos.");
            }
        }
        return year;
    }

    private void showAuthorsByYear(List<Author> authors) {
        if (authors.isEmpty()) {
            System.out.println("No se han encontrado autores para ese año.");
        } else {
            authors.stream()
                    .sorted(Comparator.comparing(Author::getBirthYear))
                    .forEach(author -> System.out.println(formatAuthor(author)));
        }
    }

// six < -- >
    private void listTop10() {
        List<Book> topBooks =bookRepository.findTop10ByOrderByDownloadCountDesc();
        topBooks.forEach(book -> System.out.println(formatBook(book)));
    }
// seven < -- >
    private void searchAuthor() {
        System.out.println("Ingresa el nombre del author a encontrar: ");
        var nameAuthor = search.nextLine();
        Optional<Author> author = authorRepository.findAuthors(nameAuthor);
        if (author.isPresent()) {
            Author foundAuthor = author.get();
            System.out.println(formatAuthor(foundAuthor));
        } else {
            System.out.println("No se encontró ningún resultado para el autor: " + nameAuthor);
        }
    }

    private void analizarDatos() {
        List<Book> books = bookRepository.findAll();
        DoubleSummaryStatistics est = books.stream()
                .filter(book -> book.getDownloadCount() > 0)
                .collect(Collectors.summarizingDouble(Book::getDownloadCount));
        System.out.println("---------------------------");
        System.out.println("Análisis de las descargas de los libros:");
        System.out.println("Promedio de descargas: " + Math.round(est.getAverage() * 100.00)/100.00);
        System.out.println("Máximo de descargas: " + est.getMax());
        System.out.println("Mínimo de descargas: " + est.getMin());
        System.out.println("Número de registros evaluados: " + est.getCount());
        System.out.println("---------------------------");

    }


    private String formatBook(Book book) {
        return """
                |||||||||||||||||||||||||||||||||||||||||||||||||||||||
                Título: %s
                Autor(es): %s
                Idioma: == > %s  < ==
                Número de descargas: %d
                |||||||||||||||||||||||||||||||||||||||||||||||||||||||
                """.formatted(
                book.getTitle(),
                book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", ")),
                book.getLanguage(),
                book.getDownloadCount()
        );
    }

    private String formatAuthor(Author author) {
        StringBuilder formatted = new StringBuilder("""
                |||||||||||||||||||||||||||||||||||||||||||||||||||||||
                Autor: %s
                Año de nacimiento: %s
                Año de fallecimiento: %s
                Libros escritos:
                """.formatted(
                author.getName(),
                Optional.ofNullable(author.getBirthYear()).map(String::valueOf).orElse("Desconocido"),
                Optional.ofNullable(author.getDeathYear()).map(String::valueOf).orElse("Desconocido")
        ));
        author.getBooks().forEach(book -> formatted.append(" * ").append(book.getTitle()).append("\n"));
        formatted.append("|||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        return formatted.toString();
    }
// eight  < -- >
    private void showDownloadStatistics() {
        var json = consumeAPI.getData(URL_BASE);
        var data = convertData.getData(json, Data.class);

        DoubleSummaryStatistics dss = data.results().stream()
                .filter(d -> d.downloadCount() > 0)
                .collect(Collectors.summarizingDouble(DataBook::downloadCount));

        System.out.println("Estadísticas de descargas:");
        System.out.printf("Cantidad media de descargas: %.2f%n", dss.getAverage());
        System.out.printf("Cantidad máxima de descargas: %.0f%n", dss.getMax());
        System.out.printf("Cantidad mínima de descargas: %.0f%n", dss.getMin());
        System.out.printf("Cantidad de registros evaluados para calcular las estadísticas: %d%n", dss.getCount());
    }
}


