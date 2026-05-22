package edu.eci.userService.config;

import edu.eci.userService.enums.SchoolRelation;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SchoolRelationConverter implements AttributeConverter<SchoolRelation, String> {

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
            throw new IllegalArgumentException(
                "Invalid SchoolRelation value in database: '" + dbData + "'. " +
                "Expected one of: STUDENT, PROFESSOR, GRADUATE");
        }
    }
}
