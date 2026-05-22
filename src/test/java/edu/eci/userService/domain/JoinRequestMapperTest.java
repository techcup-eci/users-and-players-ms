package edu.eci.userService.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import edu.eci.userService.dto.JoinRequestDTO;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.JoinRequestEntity;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.AcademicLevel;
import edu.eci.userService.enums.JoinRequestStatus;
import edu.eci.userService.enums.SchoolRelation;
import edu.eci.userService.mappers.JoinRequestMapper;
import edu.eci.userService.mappers.UserMapper;

@DisplayName("JoinRequestMapper Tests")
class JoinRequestMapperTest {

    private JoinRequestMapper joinRequestMapper;
    private UserMapper userMapper;

    private UserEntity playerEntity;
    private UserDTO playerDTO;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        joinRequestMapper = new JoinRequestMapper(userMapper);

        playerEntity = new UserEntity();
        playerEntity.setId(1L);
        playerEntity.setName("Luis Martinez");
        playerEntity.setEmail("luis@gmail.com");
        playerEntity.setSemester(0);
        playerEntity.setPhone(0L);
        playerEntity.setBirthDate(LocalDate.of(2000, 1, 1));
        playerEntity.setSchoolRelation(SchoolRelation.STUDENT);
        playerEntity.setAcademicLevel(AcademicLevel.UNDERGRADUATE);
        playerEntity.setIdentificationType("CC");
        playerEntity.setIdentificationNumber(123456L);

        playerDTO = new UserDTO();
        playerDTO.setId(1L);
        playerDTO.setName("Luis Martinez");
        playerDTO.setEmail("luis@gmail.com");
        playerDTO.setSemester(0);
        playerDTO.setPhone(0L);
    }


    @Nested
    @DisplayName("toDTO")
    class ToDTOTests {

        @Test
        @DisplayName("Debe mapear todos los campos de JoinRequestEntity a DTO")
        void toDTOShouldMapAllFields() {
            LocalDateTime now = LocalDateTime.now();
            JoinRequestEntity entity = new JoinRequestEntity();
            entity.setId(100L);
            entity.setPlayer(playerEntity);
            entity.setTeamId(5L);
            entity.setStatus(JoinRequestStatus.PENDING);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);

            JoinRequestDTO dto = joinRequestMapper.toDTO(entity);

            assertThat(dto.getId()).isEqualTo(100L);
            assertThat(dto.getTeamId()).isEqualTo(5L);
            assertThat(dto.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
            assertThat(dto.getCreatedAt()).isEqualTo(now);
            assertThat(dto.getUpdatedAt()).isEqualTo(now);
            assertThat(dto.getPlayer()).isNotNull();
            assertThat(dto.getPlayer().getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe retornar null cuando la entidad es null")
        void toDTOShouldReturnNullWhenEntityIsNull() {
            assertThat(joinRequestMapper.toDTO(null)).isNull();
        }

        @Test
        @DisplayName("Debe mapear status ACCEPTED correctamente")
        void toDTOShouldMapAcceptedStatus() {
            LocalDateTime now = LocalDateTime.now();
            JoinRequestEntity entity = new JoinRequestEntity();
            entity.setId(1L);
            entity.setPlayer(playerEntity);
            entity.setTeamId(3L);
            entity.setStatus(JoinRequestStatus.ACCEPTED);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);

            JoinRequestDTO dto = joinRequestMapper.toDTO(entity);

            assertThat(dto.getStatus()).isEqualTo(JoinRequestStatus.ACCEPTED);
        }

        @Test
        @DisplayName("Debe mapear status REJECTED correctamente")
        void toDTOShouldMapRejectedStatus() {
            LocalDateTime now = LocalDateTime.now();
            JoinRequestEntity entity = new JoinRequestEntity();
            entity.setId(2L);
            entity.setPlayer(playerEntity);
            entity.setTeamId(7L);
            entity.setStatus(JoinRequestStatus.REJECTED);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);

            JoinRequestDTO dto = joinRequestMapper.toDTO(entity);

            assertThat(dto.getStatus()).isEqualTo(JoinRequestStatus.REJECTED);
        }
    }


    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {

        @Test
        @DisplayName("Debe mapear todos los campos de JoinRequestDTO a entidad")
        void toEntityShouldMapAllFields() {
            LocalDateTime now = LocalDateTime.now();
            JoinRequestDTO dto = new JoinRequestDTO();
            dto.setId(100L);
            dto.setPlayer(playerDTO);
            dto.setTeamId(5L);
            dto.setStatus(JoinRequestStatus.PENDING);
            dto.setCreatedAt(now);
            dto.setUpdatedAt(now);

            JoinRequestEntity entity = joinRequestMapper.toEntity(dto);

            assertThat(entity.getId()).isEqualTo(100L);
            assertThat(entity.getTeamId()).isEqualTo(5L);
            assertThat(entity.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
            assertThat(entity.getCreatedAt()).isEqualTo(now);
            assertThat(entity.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Debe retornar null cuando el DTO es null")
        void toEntityShouldReturnNullWhenDTOIsNull() {
            assertThat(joinRequestMapper.toEntity(null)).isNull();
        }

        @Test
        @DisplayName("Debe mapear el player correctamente")
        void toEntityShouldMapPlayer() {
            LocalDateTime now = LocalDateTime.now();
            JoinRequestDTO dto = new JoinRequestDTO();
            dto.setId(1L);
            dto.setPlayer(playerDTO);
            dto.setTeamId(3L);
            dto.setStatus(JoinRequestStatus.PENDING);
            dto.setCreatedAt(now);
            dto.setUpdatedAt(now);

            JoinRequestEntity entity = joinRequestMapper.toEntity(dto);

            assertThat(entity.getPlayer()).isNotNull();
            assertThat(entity.getPlayer().getId()).isEqualTo(1L);
        }
    }


    @Nested
    @DisplayName("Round trip entity → DTO → entity")
    class RoundTripTests {

        @Test
        @DisplayName("Conversión ida y vuelta debe preservar datos clave")
        void roundTripShouldPreserveData() {
            LocalDateTime now = LocalDateTime.now();
            JoinRequestEntity original = new JoinRequestEntity();
            original.setId(50L);
            original.setPlayer(playerEntity);
            original.setTeamId(10L);
            original.setStatus(JoinRequestStatus.PENDING);
            original.setCreatedAt(now);
            original.setUpdatedAt(now);

            JoinRequestDTO dto = joinRequestMapper.toDTO(original);
            JoinRequestEntity reconstructed = joinRequestMapper.toEntity(dto);

            assertThat(reconstructed.getId()).isEqualTo(original.getId());
            assertThat(reconstructed.getTeamId()).isEqualTo(original.getTeamId());
            assertThat(reconstructed.getStatus()).isEqualTo(original.getStatus());
        }
    }
}
