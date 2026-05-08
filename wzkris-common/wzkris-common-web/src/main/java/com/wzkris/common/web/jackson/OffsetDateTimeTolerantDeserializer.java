package com.wzkris.common.web.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Supports both ISO-8601 offset datetime and legacy yyyy-MM-dd HH:mm:ss format.
 */
public class OffsetDateTimeTolerantDeserializer extends JsonDeserializer<OffsetDateTime> {

    private final DateTimeFormatter legacyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public OffsetDateTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        String text = parser.getValueAsString();
        if (text == null || text.isBlank()) {
            return null;
        }

        try {
            return OffsetDateTime.parse(text);
        } catch (DateTimeParseException ignored) {
            LocalDateTime localDateTime = LocalDateTime.parse(text, legacyFormatter);
            return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }
    }

}
