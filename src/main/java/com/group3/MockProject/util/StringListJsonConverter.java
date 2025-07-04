package com.group3.MockProject.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Converter
public class StringListJsonConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi convert List<String> sang JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) {
                return Collections.emptyList();
            }
            // Kiểm tra nếu dbData không phải mảng JSON (không bắt đầu bằng [ hoặc kết thúc bằng ])
            if (!dbData.startsWith("[") || !dbData.endsWith("]")) {
                return Collections.singletonList(dbData);
            }
            // Nếu là mảng JSON, deserialize thành String[]
            return Arrays.asList(objectMapper.readValue(dbData, String[].class));
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi convert JSON sang List<String>", e);
        }
    }
}