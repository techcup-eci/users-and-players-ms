package edu.eci.userService.config;

import edu.eci.userService.enums.SchoolRelation;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class SchoolRelationConverter implements AttributeConverter<SchoolRelation, String> {

    private static final Logger log = LoggerFactory.getLogger(SchoolRelationConverter.class);

    @Override
    public String convertToDatabaseColumn(SchoolRelation attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public SchoolRelation convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return null;
        try {
            return SchoolRelation.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Unknown SchoolRelation value in database: '{}'. Returning null.", dbData);
            return null;
        }
    }
}
