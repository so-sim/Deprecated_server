package com.sosim.server.common.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class StringToLocalDateTimeConverter extends StdConverter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String value) {
        return Timestamp.valueOf(value).toLocalDateTime();
    }
}
