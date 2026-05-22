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
        dto.setSchoolRelation(entity.getSchoolRelation());
        dto.setAcademicLevel(entity.getAcademicLevel());
        dto.setProfessorType(entity.getProfessorType());
        dto.setAcademicProgram(entity.getAcademicProgram());
        dto.setSemester(entity.getSemester() != null ? entity.getSemester() : 0);
        dto.setIdentificationType(entity.getIdentificationType());
        dto.setIdentificationNumber(entity.getIdentificationNumber());
        dto.setPhone(entity.getPhone());
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
        entity.setSchoolRelation(dto.getSchoolRelation());
        entity.setAcademicLevel(dto.getAcademicLevel());
        entity.setProfessorType(dto.getProfessorType());
        entity.setAcademicProgram(dto.getAcademicProgram());
        entity.setSemester(dto.getSemester());
        entity.setIdentificationType(dto.getIdentificationType());
        entity.setIdentificationNumber(dto.getIdentificationNumber());
        entity.setPhone(dto.getPhone() != null ? dto.getPhone() : 0L);
        return entity;
    }
}
