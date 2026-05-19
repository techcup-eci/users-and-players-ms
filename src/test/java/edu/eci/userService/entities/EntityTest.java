package edu.eci.userService.entities;

import edu.eci.userService.enums.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTest {

    @Test
    @DisplayName("UserEntity: Getters and Setters")
    void userEntityTest() {
        UserEntity entity = new UserEntity();
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        AthleticProfileEntity profile = new AthleticProfileEntity();
        
        entity.setId(1L);
        entity.setName("Name");
        entity.setEmail("email@test.com");
        entity.setBirthDate(birthDate);
        entity.setRole(UserRoleEnum.STUDENT);
        entity.setRelationship("staff");
        entity.setAcademicProgram("Program");
        entity.setSemester(1);
        entity.setIdentificationType("CC");
        entity.setIdentificationNumber(123L);
        entity.setPhone(456L);
        entity.setSystemRole("PLAYER");

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Name");
        assertThat(entity.getEmail()).isEqualTo("email@test.com");
        assertThat(entity.getBirthDate()).isEqualTo(birthDate);
        assertThat(entity.getRole()).isEqualTo(UserRoleEnum.STUDENT);
        assertThat(entity.getRelationship()).isEqualTo("staff");
        assertThat(entity.getAcademicProgram()).isEqualTo("Program");
        assertThat(entity.getSemester()).isEqualTo(1);
        assertThat(entity.getIdentificationType()).isEqualTo("CC");
        assertThat(entity.getIdentificationNumber()).isEqualTo(123L);
        assertThat(entity.getPhone()).isEqualTo(456L);
        assertThat(entity.getSystemRole()).isEqualTo("PLAYER");
    }

    @Test
    @DisplayName("AthleticProfileEntity: Getters and Setters")
    void athleticProfileEntityTest() {
        AthleticProfileEntity entity = new AthleticProfileEntity();
        UserEntity user = new UserEntity();
        
        entity.setId(1L);
        entity.setDorsalNumber(10);
        entity.setNickName("Nick");
        entity.setPosition("Pos");
        entity.setLaterality("Lat");
        entity.setStature("180");
        entity.setState("Active");
        entity.setUser(user);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getDorsalNumber()).isEqualTo(10);
        assertThat(entity.getNickName()).isEqualTo("Nick");
        assertThat(entity.getPosition()).isEqualTo("Pos");
        assertThat(entity.getLaterality()).isEqualTo("Lat");
        assertThat(entity.getStature()).isEqualTo("180");
        assertThat(entity.getState()).isEqualTo("Active");
        assertThat(entity.getUser()).isEqualTo(user);
    }
}
