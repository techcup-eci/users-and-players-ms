package edu.eci.userService.config;

import edu.eci.userService.enums.AcademicLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class AcademicLevelConverter implements AttributeConverter<AcademicLevel, String> {

    private static final Logger log = LoggerFactory.getLogger(AcademicLevelConverter.class);

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
            log.warn("Unknown AcademicLevel value in database: '{}'. Returning null.", dbData);
            return null;
        }
    }
}
