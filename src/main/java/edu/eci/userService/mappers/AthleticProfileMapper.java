package edu.eci.userService.mappers;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;

public class AthleticProfileMapper {

    public static AthleticProfileDTO toDTO(AthleticProfileEntity entity) {
        AthleticProfileDTO dto = new AthleticProfileDTO();
        dto.setDorsalNumber(entity.getDorsalNumber());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setPosition(entity.getPosition());
        dto.setLaterality(entity.getLaterality());
        dto.setStature(entity.getStature());
        dto.setState(entity.getState());
        return dto;
    }
}