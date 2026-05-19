package edu.eci.userService.mappers;

import org.springframework.stereotype.Component;
import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;

@Component
public class AthleticProfileMapper {

    public AthleticProfileDTO toDTO(AthleticProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        AthleticProfileDTO dto = new AthleticProfileDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setDorsalNumber(entity.getDorsalNumber());
        dto.setPosition(entity.getPosition());
        dto.setLaterality(entity.getLaterality());
        dto.setStature(entity.getStature());
        dto.setStatus(entity.getStatus());
        dto.setPhotoUrl(entity.getPhotoUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public AthleticProfileEntity toEntity(AthleticProfileDTO dto) {
        if (dto == null) {
            return null;
        }
        AthleticProfileEntity entity = new AthleticProfileEntity();
        entity.setId(dto.getId());
        entity.setDorsalNumber(dto.getDorsalNumber());
        entity.setPosition(dto.getPosition());
        entity.setLaterality(dto.getLaterality());
        entity.setStature(dto.getStature());
        entity.setStatus(dto.getStatus());
        entity.setPhotoUrl(dto.getPhotoUrl());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

}
