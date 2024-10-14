package com.toby959.challenge.main;

import com.toby959.challenge.model.Data;
import com.toby959.challenge.model.DataBook;
import com.toby959.challenge.service.ConsumeAPI;
import com.toby959.challenge.service.ConvertData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumeAPI consumeAPI = new ConsumeAPI();
    private ConvertData convertData = new ConvertData();
    private Scanner search = new Scanner(System.in);

    public void showTheMenu() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("#######################################################");
            System.out.println("¡Bienvenido a la consulta de libros!");
            System.out.println("Seleccione una opción:");
            System.out.println("1. Ver los 10 libros más descargados");
            System.out.println("2. Buscar un libro por nombre");
            System.out.println("3. Ver estadísticas de descargas");
            System.out.println("4. Salir");
            System.out.println("#######################################################");

            int option = Integer.parseInt(search.nextLine().trim());

            switch (option) {
                case 1:
                    showTopBooks();
                    break;
                case 2:
                    searchBookByName();
                    break;
                case 3:
                    showDownloadStatistics();
                    break;
                case 4:
                    continuar = false;
                    System.out.println("Saliendo del programa. ¡Hasta pronto!!!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción correcta.");
                    break;
            }
        }
    }
    private void showTopBooks() {
        var json = consumeAPI.getData(URL_BASE);
        var data = convertData.getData(json, Data.class);

        // -- Top 10 --
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("Top 10 Libros más descargados:");
        System.out.println(" ");

        data.bookList().stream()
                .sorted(Comparator.comparing(DataBook::downloadCount))     // DataBook::downloadCount).reversed())
                .limit(10)
                .map(b -> b.title().toUpperCase())
                .forEach(System.out::println);

        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||");
    }

    private void searchBookByName() {
        try {
            System.out.println("Ingrese el nombre del libro que desea buscar:");
            var bookTitle = search.nextLine();

            String encodedTitle = URLEncoder.encode(bookTitle, StandardCharsets.UTF_8.toString());
            String url = URL_BASE + "?search=" + encodedTitle;

            var json = consumeAPI.getData(url);
            var getData = convertData.getData(json, Data.class);

            Optional<DataBook> getBook = getData.bookList().stream()
                    .filter(b -> b.title().toUpperCase().contains(bookTitle.toUpperCase()))
                    .findFirst();

            if (getBook.isPresent()) {
                System.out.println("Libro Encontrado:");
                System.out.println(getBook.get());
            } else {
                System.out.println("Libro no encontrado!!!");
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error al codificar el título del libro: " + e.getMessage());
        }
    }
    private void showDownloadStatistics() {
        var json = consumeAPI.getData(URL_BASE);
        var data = convertData.getData(json, Data.class);

        DoubleSummaryStatistics dss = data.bookList().stream()
                .filter(d -> d.downloadCount() > 0)
                .collect(Collectors.summarizingDouble(DataBook::downloadCount));

        System.out.println("Estadísticas de descargas:");
        System.out.printf("Cantidad media de descargas: %.2f%n", dss.getAverage());
        System.out.printf("Cantidad máxima de descargas: %.0f%n", dss.getMax());
        System.out.printf("Cantidad mínima de descargas: %.0f%n", dss.getMin());
        System.out.printf("Cantidad de registros evaluados para calcular las estadísticas: %d%n", dss.getCount());
    }
}


