package com.toby959.challenge.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ConsumeAPI {

    private final HttpClient client;

    public ConsumeAPI() {
        this.client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public String getData(String url) {
        HttpRequest request = createRequest(url);
        return sendRequest(request);
    }

    private HttpRequest createRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
    }

    private String sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            validateResponse(response);
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al obtener datos de la API", e);
        }
    }

    private void validateResponse(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en la solicitud HTTP: " + response.statusCode());
        }
    }
}
