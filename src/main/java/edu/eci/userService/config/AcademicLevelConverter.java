package edu.eci.userService.config;

import edu.eci.userService.enums.AcademicLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AcademicLevelConverter implements AttributeConverter<AcademicLevel, String> {

    @Override
    public String convertToDatabaseColumn(AcademicLevel attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public AcademicLevel convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return null;
        try {
            return AcademicLevel.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid AcademicLevel value in database: '" + dbData + "'. " +
                "Expected one of: UNDERGRADUATE, POSTGRADUATE, MASTER");
        }
    }
}
