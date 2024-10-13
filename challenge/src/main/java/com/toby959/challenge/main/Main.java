package com.toby959.challenge.main;

import com.toby959.challenge.model.Data;
import com.toby959.challenge.model.DataBook;
import com.toby959.challenge.service.ConsumeAPI;
import com.toby959.challenge.service.ConvertData;

import java.util.Comparator;


public class Main {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumeAPI consumeAPI = new ConsumeAPI();
    private ConvertData convertData = new ConvertData();

    public void showTheMenu() {

        var josn = consumeAPI.getData(URL_BASE);
        System.out.println(josn);
        var data = convertData.getData(josn, Data.class);
        System.out.println(data);

// -- Top 10 --
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("Top 10 Libros más descargados");
        data.bookList().stream().sorted(Comparator.comparing(DataBook::numberOfDownloads).reversed())
                .limit(10)
                .map(b ->b.title().toUpperCase())
                .forEach(System.out::println);



    }

}
