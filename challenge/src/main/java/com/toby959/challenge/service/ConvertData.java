package com.toby959.challenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertData implements IConvertData {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T getData(String json, Class<T> obj) {
        try {
            return mapper.readValue(json.toString(), obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
