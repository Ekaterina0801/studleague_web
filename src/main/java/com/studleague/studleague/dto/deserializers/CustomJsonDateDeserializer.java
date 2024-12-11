package com.studleague.studleague.dto.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

public class CustomJsonDateDeserializer extends JsonDeserializer<LocalDate>
{
    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        return LocalDate.parse(date); // Will parse "yyyy-MM-dd" format by default
    }


}
