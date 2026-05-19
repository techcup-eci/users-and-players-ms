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
        dto.setRelationship(entity.getRelationship());
        dto.setAcademicProgram(entity.getAcademicProgram());
        dto.setSemester(entity.getSemester());
        dto.setIdentificationType(entity.getIdentificationType());
        dto.setIdentificationNumber(entity.getIdentificationNumber());
        dto.setPhone(entity.getPhone());
        dto.setSystemRole(entity.getSystemRole());
        return dto;
    }

    public UserEntity toEntity(UserDTO dto) {
        if (dto == null) { return null; }

        UserEntity entity = new UserEntity();
        if (dto.getId() > 0) {
            entity.setId(dto.getId());
        }
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setBirthDate(dto.getBirthDate());
        entity.setRole(dto.getRole());
        entity.setRelationship(dto.getRelationship());
        entity.setAcademicProgram(dto.getAcademicProgram());
        entity.setSemester(dto.getSemester());
        entity.setIdentificationType(dto.getIdentificationType());
        entity.setIdentificationNumber(dto.getIdentificationNumber());
        entity.setPhone(dto.getPhone() != null ? dto.getPhone() : 0L);
        entity.setSystemRole(dto.getSystemRole());
        return entity;
    }
}
