package com.group3.MockProject.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Converter
public class AttachedFileJsonConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            if (attribute == null || attribute.isEmpty()) {
                return null;
            }
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi convert List<String> sang JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) {
                return new ArrayList<>();
            }

            JsonNode jsonNode = objectMapper.readTree(dbData);
            List<String> result = new ArrayList<>();

            if (jsonNode.isArray()) {
                for (JsonNode item : jsonNode) {
                    result.add(item.asText());
                }
            } else if (jsonNode.isObject() && jsonNode.has("filename")) {
                result.add(jsonNode.get("filename").asText());
            } else if (jsonNode.isTextual()) {
                result.add(jsonNode.asText());
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi convert JSON sang List<String>: " + dbData, e);
        }
    }
} 