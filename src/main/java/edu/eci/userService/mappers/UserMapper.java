package edu.eci.userService.mappers;

import org.springframework.stereotype.Component;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.UserEntity;

@Component
public class UserMapper {

    public UserDTO toDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setBirthDate(entity.getBirthDate());
        dto.setPhone(entity.getPhone());
        dto.setIdentificationType(entity.getIdentificationType());
        dto.setIdentificationNumber(entity.getIdentificationNumber());
        dto.setSchoolRelation(entity.getSchoolRelation());
        dto.setAcademicLevel(entity.getAcademicLevel());
        dto.setSemester(entity.getSemester());
        dto.setAcademicProgram(entity.getAcademicProgram());
        dto.setProfessionalChair(entity.getProfessionalChair());
        dto.setPlantType(entity.getPlantType());
        dto.setRole(entity.getRole());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public UserEntity toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setBirthDate(dto.getBirthDate());
        entity.setPhone(dto.getPhone());
        entity.setIdentificationType(dto.getIdentificationType());
        entity.setIdentificationNumber(dto.getIdentificationNumber());
        entity.setSchoolRelation(dto.getSchoolRelation());
        entity.setAcademicLevel(dto.getAcademicLevel());
        entity.setSemester(dto.getSemester());
        entity.setAcademicProgram(dto.getAcademicProgram());
        entity.setProfessionalChair(dto.getProfessionalChair());
        entity.setPlantType(dto.getPlantType());
        entity.setRole(dto.getRole());
        entity.setStatus(dto.getStatus());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
