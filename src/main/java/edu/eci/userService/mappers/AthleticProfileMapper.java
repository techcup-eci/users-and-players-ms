package edu.eci.userService.mappers;

import org.springframework.stereotype.Component;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.entities.UserEntity;

@Component
public class AthleticProfileMapper {

    private final UserMapper userMapper;

    public AthleticProfileMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public AthleticProfileDTO toDTO(AthleticProfileEntity entity) {
        if (entity == null) {
            return null;
        }

        AthleticProfileDTO dto = new AthleticProfileDTO();
        dto.setDorsalNumber(entity.getDorsalNumber());
        dto.setId(entity.getId());
        dto.setUser(userMapper.toDTO(entity.getUser())); // ← convierte a UserDTO, sin password
        dto.setEmail(entity.getUser() != null ? entity.getUser().getEmail() : null);
        dto.setNickName(entity.getNickName());
        dto.setPosition(entity.getPosition());
        dto.setLaterality(entity.getLaterality());
        dto.setStature(entity.getStature());
        dto.setState(entity.getState());
        return dto;
    }

    public AthleticProfileEntity toEntity(AthleticProfileDTO dto) {
        if (dto == null) {
            return null;
        }

        AthleticProfileEntity entity = new AthleticProfileEntity();
        entity.setDorsalNumber(dto.getDorsalNumber());
        entity.setId(dto.getId());
        if (dto.getUser() != null) {
            entity.setUser(userMapper.toEntity(dto.getUser())); // ← convierte de UserDTO a UserEntity
        }
        entity.setNickName(dto.getNickName());
        entity.setPosition(dto.getPosition());
        entity.setLaterality(dto.getLaterality());
        entity.setStature(dto.getStature());
        entity.setState(dto.getState());
        return entity;
    }
}