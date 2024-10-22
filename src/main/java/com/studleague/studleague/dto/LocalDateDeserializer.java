package com.studleague.studleague.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateTimeString = p.getText();
        // Extract the date part
        String dateString = dateTimeString.split("T")[0]; // Get the date part before 'T'
        return LocalDate.parse(dateString); // Parse to LocalDate
    }
}

