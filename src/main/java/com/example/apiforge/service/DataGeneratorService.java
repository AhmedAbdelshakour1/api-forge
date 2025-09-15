package com.example.apiforge.service;

import com.example.apiforge.entity.EntitySchema;
import com.example.apiforge.entity.SchemaField;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataGeneratorService {
    private final Faker faker = new Faker(new Locale("en"));

    public Map<String, Object> generateOne(EntitySchema schema){
        Map<String, Object> data = new HashMap<>();
        for(var field : schema.getFields()) {
            data.put(field.getFieldName(), generateValueForField(field));
        }
        return data;
    }

    public List<Map<String, Object>> generateMany(EntitySchema schema, int count){
        List<Map<String, Object>> data = new ArrayList<>();
        for(int i = 0; i < count; i++){
            data.add(generateOne(schema));
        }
        return data;
    }

    private Object generateValueForField(SchemaField field) {
        if(field.getArray() == Boolean.TRUE){
            return generateArray(field);
        }

        return switch (field.getFieldType()) {
            case STRING -> generateString(field);
            case NUMBER -> generateNumber(field);
            case BOOLEAN -> faker.bool().bool();
            case DATE -> faker.date().birthday();
            case EMAIL -> faker.internet().emailAddress();
            case UUID -> faker.internet().uuid();
            case OBJECT -> generateObject(field);
            case ARRAY -> generateArray(field);
        };
    }

    private Object generateArray(SchemaField field) {
        int size = faker.number().numberBetween(field.getMinLength(), field.getMaxLength());
        List<Object> array = new ArrayList<>();
        for(int i = 0; i < size; i++){
            switch (field.getFieldType()) {
                case STRING -> array.add(generateString(field));
                case NUMBER -> array.add(generateNumber(field));
                case BOOLEAN -> array.add(faker.bool().bool());
                case DATE -> array.add(faker.date().birthday());
                case EMAIL -> array.add(faker.internet().emailAddress());
                case UUID -> array.add(faker.internet().uuid());
                case OBJECT -> array.add(generateObject(field));
            }
        }
        return array;
    }

    // TODO: Implement a more complex object generator based on nested schema fields
    private Object generateObject(SchemaField field) {
        return Map.of("sampleKey", "sampleValue");
    }

    private Object generateNumber(SchemaField field) {
        int min = field.getMinValue() != null ? field.getMinValue() : 0;
        int max = field.getMaxValue() != null ? field.getMaxValue() : 100;
        return faker.number().numberBetween(min, max + 1);
    }

    private Object generateString(SchemaField field) {
        int min = field.getMinLength() != null ? field.getMinLength() : 3;
        int max = field.getMaxLength() != null ? field.getMaxLength() : 10;
        int length = faker.number().numberBetween(min, max + 1);
        return faker.name().username().substring(0, Math.min(length, 10));
    }
}
