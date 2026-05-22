package edu.eci.userService.config;

import edu.eci.userService.enums.ProfessorType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class ProfessorTypeConverter implements AttributeConverter<ProfessorType, String> {

    private static final Logger log = LoggerFactory.getLogger(ProfessorTypeConverter.class);

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
            log.warn("Unknown ProfessorType value in database: '{}'. Returning null.", dbData);
            return null;
        }
    }
}
