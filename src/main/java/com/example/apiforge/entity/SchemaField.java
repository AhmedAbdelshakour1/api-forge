package com.example.apiforge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.File;
import java.time.LocalDateTime;

@Entity
@Table(name = "schema_fields")
public class SchemaField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "field_name", nullable = false, length = 50)
    @NotBlank(message = "Field name is required")
    @Size(max = 50, message = "Field name cannot exceed 50 characters")
    private String fieldName;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false)
    private FieldType fieldType;

    @Column(name = "is_required")
    private Boolean isRequired = true;

    @Column(name = "is_array")
    private Boolean isArray = false;

    @Column(name = "min_value")
    private Integer minValue;

    @Column(name = "max_value")
    private Integer maxValue;

    @Column(name = "min_length")
    private Integer minLength;

    @Column(name = "max_length")
    private Integer maxLength;

    @Column(name = "format_pattern", length = 100)
    private String formatPattern;

    @Column(name = "default_value", length = 500)
    private String defaultValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schema_id", nullable = false)
    private EntitySchema entitySchema;

    public enum FieldType {
        STRING("string"),
        NUMBER("number"),
        BOOLEAN("boolean"),
        DATE("date"),
        EMAIL("email"),
        UUID("uuid"),
        ARRAY("array"),
        OBJECT("object");

        private final String value;

        FieldType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public SchemaField() {}

    public SchemaField(String fieldName, FieldType fieldType, Boolean isRequired, EntitySchema entitySchema) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.isRequired = isRequired;
        this.entitySchema = entitySchema;
    }

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Boolean getArray() {
        return isArray;
    }

    public void setArray(Boolean array) {
        isArray = array;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getFormatPattern() {
        return formatPattern;
    }

    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EntitySchema getEntitySchema() {
        return entitySchema;
    }

    public void setEntitySchema(EntitySchema entitySchema) {
        this.entitySchema = entitySchema;
    }

    @Override
    public String toString() {
        return "SchemaField{" +
                "id=" + id +
                ", fieldName='" + fieldName + '\'' +
                ", fieldType=" + fieldType +
                ", isRequired=" + isRequired +
                ", isArray=" + isArray +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", minLength=" + minLength +
                ", maxLength=" + maxLength +
                ", formatPattern='" + formatPattern + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", createdAt=" + createdAt +
                ", entitySchema=" + entitySchema +
                '}';
    }
}
