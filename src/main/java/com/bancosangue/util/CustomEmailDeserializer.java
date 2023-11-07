package com.bancosangue.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CustomEmailDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String email = node.asText();
        // Handle unescaped control characters here, e.g., remove them
        email = email.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", ""); // This removes control characters except for \r, \n, and \t
        return email;
    }
}