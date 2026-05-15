package edu.eci.userService.dto;

import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DTOTest {

    @Test
    @DisplayName("UserDTO: Getters and Setters")
    void userDTOTest() {
        UserDTO dto = new UserDTO();
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        
        dto.setId(1L);
        dto.setName("Name");
        dto.setEmail("email@test.com");
        dto.setBirthDate(birthDate);
        dto.setRole(UserRoleEnum.STUDENT);
        dto.setRelationShip("staff");
        dto.setAcademicProgram("Program");
        dto.setSemester(1);
        dto.setIdentificationType("CC");
        dto.setIdentificationNumber(123L);
        dto.setPhone(456L);
        dto.setPassword("pass");

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Name");
        assertThat(dto.getEmail()).isEqualTo("email@test.com");
        assertThat(dto.getBirthDate()).isEqualTo(birthDate);
        assertThat(dto.getRole()).isEqualTo(UserRoleEnum.STUDENT);
        assertThat(dto.getRelationShip()).isEqualTo("staff");
        assertThat(dto.getAcademicProgram()).isEqualTo("Program");
        assertThat(dto.getSemester()).isEqualTo(1);
        assertThat(dto.getIdentificationType()).isEqualTo("CC");
        assertThat(dto.getIdentificationNumber()).isEqualTo(123L);
        assertThat(dto.getPhone()).isEqualTo(456L);
        assertThat(dto.getPassword()).isEqualTo("pass");
    }

    @Test
    @DisplayName("AthleticProfileDTO: Getters and Setters")
    void athleticProfileDTOTest() {
        AthleticProfileDTO dto = new AthleticProfileDTO();
        UserEntity user = new UserEntity();
        
        dto.setId(1L);
        dto.setDorsalNumber(10);
        dto.setNickName("Nick");
        dto.setPosition("Pos");
        dto.setLaterality("Lat");
        dto.setStature("180");
        dto.setState("Active");
        dto.setUser(user);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDorsalNumber()).isEqualTo(10);
        assertThat(dto.getNickName()).isEqualTo("Nick");
        assertThat(dto.getPosition()).isEqualTo("Pos");
        assertThat(dto.getLaterality()).isEqualTo("Lat");
        assertThat(dto.getStature()).isEqualTo("180");
        assertThat(dto.getState()).isEqualTo("Active");
        assertThat(dto.getUser()).isEqualTo(user);
    }
}
