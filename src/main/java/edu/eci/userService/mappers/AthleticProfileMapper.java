package edu.eci.userService.mappers;

import org.springframework.stereotype.Component;
import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;

@Component
public class AthleticProfileMapper {

    public AthleticProfileDTO toDTO(AthleticProfileEntity entity) {
        AthleticProfileDTO dto = new AthleticProfileDTO();
        dto.setDorsalNumber(entity.getDorsalNumber());
        dto.setEmail(entity.getEmail());
        dto.setPosition(entity.getPosition());
        dto.setLaterality(entity.getLaterality());
        dto.setStature(entity.getStature());
        dto.setState(entity.getState());
        return dto;
    }

    public AthleticProfileEntity toEntity(AthleticProfileDTO dto) {
        AthleticProfileEntity entity = new AthleticProfileEntity();
        entity.setDorsalNumber(dto.getDorsalNumber());
        entity.setEmail(dto.getEmail());
        entity.setPosition(dto.getPosition());
        entity.setLaterality(dto.getLaterality());
        entity.setStature(dto.getStature());
        entity.setState(dto.getState());
        return entity;
    }

}
