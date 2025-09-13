package com.example.apiforge.validator;

import com.example.apiforge.dto.entityfieldDTO.SchemaFieldRequestDto;
import org.springframework.stereotype.Component;

@Component
public class SchemaFieldValidator {
    public void validate(SchemaFieldRequestDto requestDto) {
        // Field name pattern
        if(requestDto.getFieldName() == null || !requestDto.getFieldName().matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException("Invalid field name format");
        }

        // Field type validation
        String type = requestDto.getFieldType();
        switch (type) {
            case "string" -> validateString(requestDto);
            case "number" -> validateNumber(requestDto);
            case "array" -> validateArray(requestDto);
            case "object" -> validateObject(requestDto);
            case "date" -> validateDate(requestDto);
            case "email", "uuid", "boolean" -> {

            }
            default -> throw new IllegalArgumentException("Unsupported field type: " + type);
        }
    }
    private void validateString(SchemaFieldRequestDto requestDto) {
        if(requestDto.getMinLength() != null && requestDto.getMinLength() < 0) {
            throw new IllegalArgumentException("minLength must be non-negative");
        }
        if(requestDto.getMaxLength() != null && requestDto.getMaxLength() < 0) {
            throw new IllegalArgumentException("maxLength must be non-negative");
        }
        if(requestDto.getMinLength() != null && requestDto.getMaxLength() != null &&
                requestDto.getMinLength() > requestDto.getMaxLength()) {
            throw new IllegalArgumentException("minLength cannot be greater than maxLength");
        }
    }
    private void validateNumber(SchemaFieldRequestDto requestDto) {
        if(requestDto.getMinValue() != null && requestDto.getMaxValue() != null &&
                requestDto.getMinValue() > requestDto.getMaxValue()) {
            throw new IllegalArgumentException("minValue cannot be greater than maxValue");
        }
    }
    private void validateArray(SchemaFieldRequestDto requestDto) {
        if(!requestDto.isArray()) {
            throw new IllegalArgumentException("isArray must be true for array type");
        }
    }
    private void validateObject(SchemaFieldRequestDto requestDto) {
        if(requestDto.isArray()) {
            throw new IllegalArgumentException("isArray must be false for object type");
        }
    }
    private void validateDate(SchemaFieldRequestDto requestDto) {
        if(requestDto.getFormatPattern() != null && requestDto.getFormatPattern().isEmpty()) {
            throw new IllegalArgumentException("formatPattern cannot be empty for date type");
        }
    }
}
