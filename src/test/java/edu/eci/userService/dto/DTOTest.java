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
        dto.setRelationship("staff");
        dto.setAcademicProgram("Program");
        dto.setSemester(1);
        dto.setIdentificationType("CC");
        dto.setIdentificationNumber(123L);
        dto.setPhone(456L);
        dto.setSystemRole("PLAYER");

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Name");
        assertThat(dto.getEmail()).isEqualTo("email@test.com");
        assertThat(dto.getBirthDate()).isEqualTo(birthDate);
        assertThat(dto.getRole()).isEqualTo(UserRoleEnum.STUDENT);
        assertThat(dto.getRelationship()).isEqualTo("staff");
        assertThat(dto.getAcademicProgram()).isEqualTo("Program");
        assertThat(dto.getSemester()).isEqualTo(1);
        assertThat(dto.getIdentificationType()).isEqualTo("CC");
        assertThat(dto.getIdentificationNumber()).isEqualTo(123L);
        assertThat(dto.getPhone()).isEqualTo(456L);
        assertThat(dto.getSystemRole()).isEqualTo("PLAYER");
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

    @Test
    @DisplayName("UserDTO: Constructor completo")
    void userDTOFullConstructorTest() {
        LocalDate birthDate = LocalDate.of(1999, 12, 31);

        UserDTO dto = new UserDTO(
                2L,
                "Ana",
                "ana@test.com",
                birthDate,
                UserRoleEnum.TEACHER,
                "teacher",
                "Matematicas",
                3,
                "TI",
                987L,
                654L,
                "PLAYER"
        );
        dto.setPassword("secret");

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getName()).isEqualTo("Ana");
        assertThat(dto.getEmail()).isEqualTo("ana@test.com");
        assertThat(dto.getBirthDate()).isEqualTo(birthDate);
        assertThat(dto.getRole()).isEqualTo(UserRoleEnum.TEACHER);
        assertThat(dto.getRelationship()).isEqualTo("teacher");
        assertThat(dto.getAcademicProgram()).isEqualTo("Matematicas");
        assertThat(dto.getSemester()).isEqualTo(3);
        assertThat(dto.getIdentificationType()).isEqualTo("TI");
        assertThat(dto.getIdentificationNumber()).isEqualTo(987L);
        assertThat(dto.getPhone()).isEqualTo(654L);
        assertThat(dto.getSystemRole()).isEqualTo("PLAYER");
        assertThat(dto.getPassword()).isEqualTo("secret");
    }

    @Test
    @DisplayName("AthleticProfileDTO: Constructor completo")
    void athleticProfileDTOFullConstructorTest() {
        UserEntity user = new UserEntity();
        user.setId(7L);

        AthleticProfileDTO dto = new AthleticProfileDTO(
                9,
                3L,
                user,
                "Nick",
                "defensa",
                "zurdo",
                "170",
                "activo"
        );

        assertThat(dto.getDorsalNumber()).isEqualTo(9);
        assertThat(dto.getId()).isEqualTo(3L);
        assertThat(dto.getUser()).isEqualTo(user);
        assertThat(dto.getNickName()).isEqualTo("Nick");
        assertThat(dto.getPosition()).isEqualTo("defensa");
        assertThat(dto.getLaterality()).isEqualTo("zurdo");
        assertThat(dto.getStature()).isEqualTo("170");
        assertThat(dto.getState()).isEqualTo("activo");
    }

    @Test
    @DisplayName("LoginRequest: Constructor completo")
    void loginRequestFullConstructorTest() {
        LoginRequest request = new LoginRequest("mail@test.com", "pwd");

        assertThat(request.getEmail()).isEqualTo("mail@test.com");
        assertThat(request.getPassword()).isEqualTo("pwd");
    }
}
