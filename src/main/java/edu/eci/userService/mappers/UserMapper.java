package edu.eci.userService.mappers;

import org.springframework.stereotype.Component;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.UserEntity;

@Component
public class UserMapper {

    public UserDTO toDTO(UserEntity entity) {
        if (entity == null) { return null; }

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setBirthDate(entity.getBirthDate());
        dto.setRole(entity.getRole());
        dto.setRelationShip(entity.getRelationShip());
        dto.setAcademicProgram(entity.getAcademicProgram());
        dto.setSemester(entity.getSemester());
        dto.setIdentificationType(entity.getIdentificationType());
        dto.setIdentificationNumber(entity.getIdentificationNumber());
        dto.setPhone(entity.getPhone());
        return dto;
    }

    public UserEntity toEntity(UserDTO dto) {
        if (dto == null) { return null; }

        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setBirthDate(dto.getBirthDate());
        entity.setRole(dto.getRole());
        entity.setRelationShip(dto.getRelationShip());
        entity.setAcademicProgram(dto.getAcademicProgram());
        entity.setSemester(dto.getSemester());
        entity.setIdentificationType(dto.getIdentificationType());
        entity.setIdentificationNumber(dto.getIdentificationNumber());
        entity.setPhone(dto.getPhone());
        return entity;
    }
}
