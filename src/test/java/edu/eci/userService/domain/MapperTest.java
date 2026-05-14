package edu.eci.userService.domain;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.UserRoleEnum;
import edu.eci.userService.mappers.AthleticProfileMapper;
import edu.eci.userService.mappers.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitarios para {@link UserMapper} y {@link AthleticProfileMapper}.
 *
 * Los mappers son POJOs puros — no necesitan contexto de Spring.
 * Estos tests verifican que la conversión entity↔DTO sea correcta y sin pérdida de datos.
 */
class MapperTest {

    private final UserMapper userMapper = new UserMapper();
    private final AthleticProfileMapper athleticProfileMapper = new AthleticProfileMapper();

    // ══════════════════════════════════════════════════════════════════════════
    // UserMapper
    // ══════════════════════════════════════════════════════════════════════════

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
            assertThat(dto.getRole()).isEqualTo(UserRoleEnum.STUDENT);
            assertThat(dto.getRelationShip()).isEqualTo("student");
            assertThat(dto.getAcademicProgram()).isEqualTo("Ingeniería de Sistemas");
            assertThat(dto.getSemester()).isEqualTo(5);
            assertThat(dto.getIdentificationType()).isEqualTo("CC");
            assertThat(dto.getIdentificationNumber()).isEqualTo(1000123456);
            assertThat(dto.getPhone()).isEqualTo(3001234567);
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
            assertThat(entity.getRole()).isEqualTo(UserRoleEnum.STUDENT);
            assertThat(entity.getRelationShip()).isEqualTo("student");
            assertThat(entity.getAcademicProgram()).isEqualTo("Ingeniería de Sistemas");
            assertThat(entity.getSemester()).isEqualTo(5);
            assertThat(entity.getIdentificationType()).isEqualTo("CC");
            assertThat(entity.getIdentificationNumber()).isEqualTo(1000123456);
            assertThat(entity.getPhone()).isEqualTo(3001234567);
        }

        @Test
        @DisplayName("toDTO no debe incluir la contraseña (campo excluido del DTO de respuesta)")
        void toDTOShouldNotExposePassword() {
            UserEntity entity = buildUserEntity();
            entity.setPassword("secret_hash");

            UserDTO dto = userMapper.toDTO(entity);

            // La contraseña no se mapea en la respuesta para no exponerla
            assertThat(dto.getPassword()).isNull();
        }

        @Test
        @DisplayName("Conversión ida y vuelta entity→DTO→entity preserva datos")
        void roundTripShouldPreserveData() {
            UserEntity original = buildUserEntity();
            UserDTO dto = userMapper.toDTO(original);
            UserEntity reconstructed = userMapper.toEntity(dto);

            assertThat(reconstructed.getName()).isEqualTo(original.getName());
            assertThat(reconstructed.getEmail()).isEqualTo(original.getEmail());
            assertThat(reconstructed.getRole()).isEqualTo(original.getRole());
        }

        // ── Builders ─────────────────────────────────────────────────────────

        private UserEntity buildUserEntity() {
            UserEntity e = new UserEntity();
            e.setId(1L);
            e.setName("Juan Pérez");
            e.setEmail("juan@eci.edu.co");
            e.setBirthDate(LocalDate.of(2000, 5, 15));
            e.setRole(UserRoleEnum.STUDENT);
            e.setRelationShip("student");
            e.setAcademicProgram("Ingeniería de Sistemas");
            e.setSemester(5);
            e.setIdentificationType("CC");
            e.setIdentificationNumber(1000123456);
            e.setPhone(3001234567);
            e.setPassword("secret_hash");
            return e;
        }

        private UserDTO buildUserDTO() {
            UserDTO dto = new UserDTO();
            dto.setId(1L);
            dto.setName("Juan Pérez");
            dto.setEmail("juan@eci.edu.co");
            dto.setBirthDate(LocalDate.of(2000, 5, 15));
            dto.setRole(UserRoleEnum.STUDENT);
            dto.setRelationShip("student");
            dto.setAcademicProgram("Ingeniería de Sistemas");
            dto.setSemester(5);
            dto.setIdentificationType("CC");
            dto.setIdentificationNumber(1000123456);
            dto.setPhone(3001234567);
            return dto;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // AthleticProfileMapper
    // ══════════════════════════════════════════════════════════════════════════

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
            assertThat(dto.getUser()).isNotNull();
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

        // ── Builders ─────────────────────────────────────────────────────────

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
            UserEntity user = new UserEntity();
            user.setId(1L);

            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setId(1L);
            dto.setDorsalNumber(10);
            dto.setNickName("Juancho");
            dto.setPosition("delantero");
            dto.setLaterality("diestro");
            dto.setStature("175cm");
            dto.setState("activo");
            dto.setUser(user);
            return dto;
        }
    }
}
