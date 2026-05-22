package edu.eci.userService.domain;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.AcademicLevel;
import edu.eci.userService.enums.ProfessorType;
import edu.eci.userService.enums.SchoolRelation;
import edu.eci.userService.mappers.AthleticProfileMapper;
import edu.eci.userService.mappers.UserMapper;

class MapperTest {

    private final UserMapper userMapper = new UserMapper();
    // ← AthleticProfileMapper ahora necesita UserMapper en el constructor
    private final AthleticProfileMapper athleticProfileMapper = new AthleticProfileMapper(userMapper);

    @Test
    @DisplayName("Mappers instanciados")
    void mappersAreInstantiable() {
        assertThat(userMapper).isNotNull();
        assertThat(athleticProfileMapper).isNotNull();
    }

    @Nested
    @DisplayName("UserMapper")
    class UserMapperTests {

        @Test
        @DisplayName("toDTO debe mapear todos los campos de UserEntity a UserDTO")
        void toDTOShouldMapAllFields() {
            UserEntity entity = buildUserEntity();

            UserDTO dto = userMapper.toDTO(entity);

            assertThat(dto.getId()).isEqualTo(1L);
            assertThat(dto.getName()).isEqualTo("Juan Pérez");
            assertThat(dto.getEmail()).isEqualTo("juan@eci.edu.co");
            assertThat(dto.getBirthDate()).isEqualTo(LocalDate.of(2000, 5, 15));
            assertThat(dto.getSchoolRelation()).isEqualTo(SchoolRelation.PROFESSOR);
            assertThat(dto.getAcademicLevel()).isEqualTo(AcademicLevel.MASTER);
            assertThat(dto.getProfessorType()).isEqualTo(ProfessorType.FULL_TIME);
            assertThat(dto.getAcademicProgram()).isEqualTo("Ingeniería de Sistemas");
            assertThat(dto.getSemester()).isEqualTo(5);
            assertThat(dto.getIdentificationType()).isEqualTo("CC");
            assertThat(dto.getIdentificationNumber()).isEqualTo(1000123456L);
            assertThat(dto.getPhone()).isEqualTo(3001234567L);
        }

        @Test
        @DisplayName("toEntity debe mapear todos los campos de UserDTO a UserEntity")
        void toEntityShouldMapAllFields() {
            UserDTO dto = buildUserDTO();

            UserEntity entity = userMapper.toEntity(dto);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getName()).isEqualTo("Juan Pérez");
            assertThat(entity.getEmail()).isEqualTo("juan@eci.edu.co");
            assertThat(entity.getBirthDate()).isEqualTo(LocalDate.of(2000, 5, 15));
            assertThat(entity.getSchoolRelation()).isEqualTo(SchoolRelation.PROFESSOR);
            assertThat(entity.getAcademicLevel()).isEqualTo(AcademicLevel.MASTER);
            assertThat(entity.getProfessorType()).isEqualTo(ProfessorType.FULL_TIME);
            assertThat(entity.getAcademicProgram()).isEqualTo("Ingeniería de Sistemas");
            assertThat(entity.getSemester()).isEqualTo(5);
            assertThat(entity.getIdentificationType()).isEqualTo("CC");
            assertThat(entity.getIdentificationNumber()).isEqualTo(1000123456L);
            assertThat(entity.getPhone()).isEqualTo(3001234567L);
        }

        @Test
        @DisplayName("Conversión ida y vuelta entity→DTO→entity preserva datos")
        void roundTripShouldPreserveData() {
            UserEntity original = buildUserEntity();
            UserDTO dto = userMapper.toDTO(original);
            UserEntity reconstructed = userMapper.toEntity(dto);

            assertThat(reconstructed.getName()).isEqualTo(original.getName());
            assertThat(reconstructed.getEmail()).isEqualTo(original.getEmail());
            assertThat(reconstructed.getSchoolRelation()).isEqualTo(original.getSchoolRelation());
        }

        @Test
        @DisplayName("toDTO debe retornar null cuando el entity es null")
        void toDTOShouldReturnNullWhenEntityIsNull() {
            assertThat(userMapper.toDTO(null)).isNull();
        }

        @Test
        @DisplayName("toEntity debe retornar null cuando el DTO es null")
        void toEntityShouldReturnNullWhenDTOIsNull() {
            assertThat(userMapper.toEntity(null)).isNull();
        }

        @Test
        @DisplayName("toEntity no debe setear id cuando es 0")
        void toEntityShouldIgnoreZeroId() {
            UserDTO dto = buildUserDTO();
            dto.setId(0);

            UserEntity entity = userMapper.toEntity(dto);

            assertThat(entity.getId()).isNull();
        }

        private UserEntity buildUserEntity() {
            UserEntity e = new UserEntity();
            e.setId(1L);
            e.setName("Juan Pérez");
            e.setEmail("juan@eci.edu.co");
            e.setBirthDate(LocalDate.of(2000, 5, 15));
            e.setSchoolRelation(SchoolRelation.PROFESSOR);
            e.setAcademicLevel(AcademicLevel.MASTER);
            e.setProfessorType(ProfessorType.FULL_TIME);
            e.setAcademicProgram("Ingeniería de Sistemas");
            e.setSemester(5);
            e.setIdentificationType("CC");
            e.setIdentificationNumber(1000123456L);
            e.setPhone(3001234567L);
            return e;
        }

        private UserDTO buildUserDTO() {
            UserDTO dto = new UserDTO();
            dto.setId(1L);
            dto.setName("Juan Pérez");
            dto.setEmail("juan@eci.edu.co");
            dto.setBirthDate(LocalDate.of(2000, 5, 15));
            dto.setSchoolRelation(SchoolRelation.PROFESSOR);
            dto.setAcademicLevel(AcademicLevel.MASTER);
            dto.setProfessorType(ProfessorType.FULL_TIME);
            dto.setAcademicProgram("Ingeniería de Sistemas");
            dto.setSemester(5);
            dto.setIdentificationType("CC");
            dto.setIdentificationNumber(1000123456L);
            dto.setPhone(3001234567L);
            return dto;
        }
    }

    @Nested
    @DisplayName("AthleticProfileMapper")
    class AthleticProfileMapperTests {

        @Test
        @DisplayName("toDTO debe mapear todos los campos de AthleticProfileEntity a DTO")
        void toDTOShouldMapAllFields() {
            AthleticProfileEntity entity = buildEntity();

            AthleticProfileDTO dto = athleticProfileMapper.toDTO(entity);

            assertThat(dto.getId()).isEqualTo(1L);
            assertThat(dto.getDorsalNumber()).isEqualTo(10);
            assertThat(dto.getNickName()).isEqualTo("Juancho");
            assertThat(dto.getPosition()).isEqualTo("delantero");
            assertThat(dto.getLaterality()).isEqualTo("diestro");
            assertThat(dto.getStature()).isEqualTo("175cm");
            assertThat(dto.getState()).isEqualTo("activo");
            // user ahora es UserDTO, no UserEntity
            assertThat(dto.getUser()).isNotNull();
            assertThat(dto.getUser().getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("toEntity debe mapear todos los campos del DTO a AthleticProfileEntity")
        void toEntityShouldMapAllFields() {
            AthleticProfileDTO dto = buildDTO();

            AthleticProfileEntity entity = athleticProfileMapper.toEntity(dto);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getDorsalNumber()).isEqualTo(10);
            assertThat(entity.getNickName()).isEqualTo("Juancho");
            assertThat(entity.getPosition()).isEqualTo("delantero");
            assertThat(entity.getLaterality()).isEqualTo("diestro");
            assertThat(entity.getStature()).isEqualTo("175cm");
            assertThat(entity.getState()).isEqualTo("activo");
        }

        @Test
        @DisplayName("Conversión ida y vuelta entity→DTO→entity preserva datos clave")
        void roundTripShouldPreserveData() {
            AthleticProfileEntity original = buildEntity();
            AthleticProfileDTO dto = athleticProfileMapper.toDTO(original);
            AthleticProfileEntity reconstructed = athleticProfileMapper.toEntity(dto);

            assertThat(reconstructed.getDorsalNumber()).isEqualTo(original.getDorsalNumber());
            assertThat(reconstructed.getPosition()).isEqualTo(original.getPosition());
            assertThat(reconstructed.getNickName()).isEqualTo(original.getNickName());
        }

        @Test
        @DisplayName("toDTO debe retornar null cuando el entity es null")
        void toDTOShouldReturnNullWhenEntityIsNull() {
            assertThat(athleticProfileMapper.toDTO(null)).isNull();
        }

        @Test
        @DisplayName("toEntity debe retornar null cuando el DTO es null")
        void toEntityShouldReturnNullWhenDTOIsNull() {
            assertThat(athleticProfileMapper.toEntity(null)).isNull();
        }

        private AthleticProfileEntity buildEntity() {
            UserEntity user = new UserEntity();
            user.setId(1L);
            user.setName("Juan Pérez");

            AthleticProfileEntity e = new AthleticProfileEntity();
            e.setId(1L);
            e.setDorsalNumber(10);
            e.setNickName("Juancho");
            e.setPosition("delantero");
            e.setLaterality("diestro");
            e.setStature("175cm");
            e.setState("activo");
            e.setUser(user);
            return e;
        }

        private AthleticProfileDTO buildDTO() {
            // ← ahora usa UserDTO, no UserEntity
            UserDTO userDTO = new UserDTO();
            userDTO.setId(1L);
            userDTO.setName("Juan Pérez");

            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setId(1L);
            dto.setDorsalNumber(10);
            dto.setNickName("Juancho");
            dto.setPosition("delantero");
            dto.setLaterality("diestro");
            dto.setStature("175cm");
            dto.setState("activo");
            dto.setUser(userDTO); // ← UserDTO
            return dto;
        }
    }
}