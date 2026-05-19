package edu.eci.userService.mappers;

import org.springframework.stereotype.Component;
import edu.eci.userService.dto.JoinRequestDTO;
import edu.eci.userService.entities.JoinRequestEntity;

@Component
public class JoinRequestMapper {

    private final UserMapper userMapper;

    public JoinRequestMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public JoinRequestDTO toDTO(JoinRequestEntity entity) {
        if (entity == null) {
            return null;
        }
        JoinRequestDTO dto = new JoinRequestDTO();
        dto.setId(entity.getId());
        dto.setPlayer(userMapper.toDTO(entity.getPlayer()));
        dto.setTeamId(entity.getTeamId());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public JoinRequestEntity toEntity(JoinRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        JoinRequestEntity entity = new JoinRequestEntity();
        entity.setId(dto.getId());
        entity.setPlayer(userMapper.toEntity(dto.getPlayer()));
        entity.setTeamId(dto.getTeamId());
        entity.setStatus(dto.getStatus());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
