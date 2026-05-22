package edu.eci.userService.config;

import edu.eci.userService.enums.ProfessorType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProfessorTypeConverter implements AttributeConverter<ProfessorType, String> {

    @Override
    public String convertToDatabaseColumn(ProfessorType attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public ProfessorType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return null;
        try {
            return ProfessorType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid ProfessorType value in database: '" + dbData + "'. " +
                "Expected one of: FULL_TIME, CHAIR");
        }
    }
}
