package com.toby959.challenge.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataBook(
         String title,
         List<DataAuthor> authors,
         List<String> languages,
        @JsonAlias("download_count") Double numberOfDownloads
) {

}
