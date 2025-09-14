package com.example.apiforge.service;

import com.example.apiforge.dto.entityfieldDTO.SchemaFieldRequestDto;
import com.example.apiforge.dto.entityfieldDTO.SchemaFieldResponseDto;
import com.example.apiforge.entity.EntitySchema;
import com.example.apiforge.entity.SchemaField;
import com.example.apiforge.repository.EntitySchemaRepository;
import com.example.apiforge.repository.SchemaFieldRepository;
import com.example.apiforge.validator.SchemaFieldValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemaFieldService {
    private final SchemaFieldRepository schemaFieldRepository;
    private final EntitySchemaRepository entitySchemaRepository;
    private final SchemaFieldValidator schemaFieldValidator;

    public SchemaFieldService(SchemaFieldRepository schemaFieldRepository,
                              EntitySchemaRepository entitySchemaRepository,
                              SchemaFieldValidator schemaFieldValidator) {
        this.schemaFieldRepository = schemaFieldRepository;
        this.entitySchemaRepository = entitySchemaRepository;
        this.schemaFieldValidator = schemaFieldValidator;
    }

    public SchemaFieldResponseDto createSchemaField(SchemaFieldRequestDto requestDto) {
        schemaFieldValidator.validate(requestDto);
        EntitySchema entitySchema = entitySchemaRepository.findById(requestDto.getSchemaId())
                .orElseThrow(() -> new IllegalArgumentException("Schema not found"));

        boolean exists = schemaFieldRepository.existsByEntitySchemaIdAndFieldName(requestDto.getSchemaId(), requestDto.getFieldName());
        if(exists){
            throw new IllegalArgumentException("Field with this name already exists in schema");
        }
        SchemaField field = convertToEntity(requestDto, entitySchema);
        SchemaField savedField = schemaFieldRepository.save(field);
        return convertToResponseDto(savedField);
    }

    public List<SchemaFieldResponseDto> getFieldsBySchemaId(Long schemaId){
        if (!entitySchemaRepository.existsById(schemaId)) {
            throw new IllegalArgumentException("Schema not found");
        }
        List<SchemaField> fields = schemaFieldRepository.findByEntitySchemaId(schemaId);
        return fields.stream().map(this::convertToResponseDto).toList();
    }

    public SchemaFieldResponseDto getFieldById(Long id) {
        SchemaField field = schemaFieldRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Field not found"));
        return convertToResponseDto(field);
    }

    public SchemaFieldResponseDto updateField(Long fieldId, SchemaFieldRequestDto requestDto) {
        schemaFieldValidator.validate(requestDto);
        SchemaField field = schemaFieldRepository.findById(fieldId)
                .orElseThrow(() -> new IllegalArgumentException("Field not found"));

        if(!field.getEntitySchema().getId().equals(requestDto.getSchemaId())){
            throw new IllegalArgumentException("Cannot change the schema of the field");
        }
        if(!field.getFieldName().equals(requestDto.getFieldName())){
            boolean exist = schemaFieldRepository.existsByEntitySchemaIdAndFieldName(requestDto.getSchemaId(), requestDto.getFieldName());
            if(exist){
                throw new IllegalArgumentException("Field with this name already exists in schema");
            }
            field.setFieldName(requestDto.getFieldName());
        }
        field.setFieldType(SchemaField.FieldType.valueOf(requestDto.getFieldType().toUpperCase()));
        field.setRequired(requestDto.isRequired());
        field.setArray(requestDto.isArray());
        field.setMinValue(requestDto.getMinValue());
        field.setMaxValue(requestDto.getMaxValue());
        field.setMinLength(requestDto.getMinLength());
        field.setMaxLength(requestDto.getMaxLength());
        field.setFormatPattern(requestDto.getFormatPattern());
        field.setDefaultValue(requestDto.getDefaultValue());

        SchemaField savedField = schemaFieldRepository.save(field);
        return convertToResponseDto(savedField);
    }

    public void deleteField(Long fieldId) {
        SchemaField field = schemaFieldRepository.findById(fieldId)
                .orElseThrow(() -> new IllegalArgumentException("Field not found"));
        schemaFieldRepository.delete(field);
    }

    private SchemaField convertToEntity(SchemaFieldRequestDto requestDto, EntitySchema entitySchema) {
        SchemaField field = new SchemaField();
        field.setFieldName(requestDto.getFieldName());
        field.setFieldType(SchemaField.FieldType.valueOf(requestDto.getFieldType().toUpperCase()));
        field.setRequired(requestDto.isRequired());
        field.setArray(requestDto.isArray());
        field.setMinValue(requestDto.getMinValue());
        field.setMaxValue(requestDto.getMaxValue());
        field.setMinLength(requestDto.getMinLength());
        field.setMaxLength(requestDto.getMaxLength());
        field.setFormatPattern(requestDto.getFormatPattern());
        field.setDefaultValue(requestDto.getDefaultValue());
        field.setEntitySchema(entitySchema);
        return field;
    }

    private SchemaFieldResponseDto convertToResponseDto(SchemaField field) {
        SchemaFieldResponseDto responseDto = new SchemaFieldResponseDto();
        responseDto.setId(field.getId());
        responseDto.setFieldName(field.getFieldName());
        responseDto.setFieldType(field.getFieldType().name().toLowerCase());
        responseDto.setRequired(field.getRequired());
        responseDto.setArray(field.getArray());
        responseDto.setMinValue(field.getMinValue());
        responseDto.setMaxValue(field.getMaxValue());
        responseDto.setMinLength(field.getMinLength());
        responseDto.setMaxLength(field.getMaxLength());
        responseDto.setFormatPattern(field.getFormatPattern());
        responseDto.setDefaultValue(field.getDefaultValue());
        responseDto.setSchemaId(field.getEntitySchema().getId());
        return responseDto;
    }


}
