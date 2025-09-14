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
        return switch (field.getFieldType()) {
            case STRING -> faker.lorem().word();
            case NUMBER -> faker.number().numberBetween(0, 1000);
            case BOOLEAN -> faker.bool().bool();
            case DATE -> faker.date().birthday();
            case EMAIL -> faker.internet().emailAddress();
            case UUID -> faker.internet().uuid();
            case ARRAY -> new Object[]{faker.lorem().word(), faker.lorem().word(), faker.lorem().word()};
            case OBJECT -> Map.of("key", faker.lorem().word());
        };
    }
}
