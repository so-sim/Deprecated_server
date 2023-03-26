package com.sosim.server.common.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTimeConverter extends StdConverter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String stringDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return LocalDateTime.parse(stringDate, dateTimeFormatter);
//        return Timestamp.valueOf(stringDate).toLocalDateTime();
    }
}
