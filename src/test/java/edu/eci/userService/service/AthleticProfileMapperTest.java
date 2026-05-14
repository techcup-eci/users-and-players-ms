package edu.eci.userService.service;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.mappers.AthleticProfileMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for AthleticProfileMapper.
 * No mocks needed. The mapper is tested directly since it has no dependencies.
 * Adapted to the actual fields: dorsalNumber, position, laterality, stature, state, nickName.
 */
@DisplayName("AthleticProfileMapper - Unit Tests")
class AthleticProfileMapperTest {

    private AthleticProfileMapper mapper;
    private AthleticProfileEntity baseEntity;
    private AthleticProfileDTO baseDTO;

    @BeforeEach
    void setUp() {
        mapper = new AthleticProfileMapper();

        baseEntity = new AthleticProfileEntity();
        baseEntity.setDorsalNumber(10);
        baseEntity.setPosition("FORWARD");
        baseEntity.setLaterality("RIGHT");
        baseEntity.setStature("180cm");
        baseEntity.setState("ACTIVE");
        baseEntity.setNickName("El Pibe");

        baseDTO = new AthleticProfileDTO();
        baseDTO.setDorsalNumber(10);
        baseDTO.setPosition("FORWARD");
        baseDTO.setLaterality("RIGHT");
        baseDTO.setStature("180cm");
        baseDTO.setState("ACTIVE");
        baseDTO.setNickName("El Pibe");
    }

    @Nested @DisplayName("toDTO - Entity to DTO conversion")
    class ToDTOTests {

        @Test @DisplayName("Must return a non-null DTO when entity is valid")
        void mustReturnNonNullDTOWhenEntityIsValid() {
            assertThat(mapper.toDTO(baseEntity)).isNotNull();
        }

        @Test @DisplayName("Must map dorsalNumber from entity to DTO correctly")
        void mustMapDorsalNumberFromEntityToDTOCorrectly() {
            assertThat(mapper.toDTO(baseEntity).getDorsalNumber()).isEqualTo(10);
        }

        @Test @DisplayName("Must map position from entity to DTO correctly")
        void mustMapPositionFromEntityToDTOCorrectly() {
            assertThat(mapper.toDTO(baseEntity).getPosition()).isEqualTo("FORWARD");
        }

        @Test @DisplayName("Must map laterality from entity to DTO correctly")
        void mustMapLateralityFromEntityToDTOCorrectly() {
            assertThat(mapper.toDTO(baseEntity).getLaterality()).isEqualTo("RIGHT");
        }

        @Test @DisplayName("Must map stature from entity to DTO correctly")
        void mustMapStatureFromEntityToDTOCorrectly() {
            assertThat(mapper.toDTO(baseEntity).getStature()).isEqualTo("180cm");
        }

        @Test @DisplayName("Must map state from entity to DTO correctly")
        void mustMapStateFromEntityToDTOCorrectly() {
            assertThat(mapper.toDTO(baseEntity).getState()).isEqualTo("ACTIVE");
        }

        @Test @DisplayName("Must map nickName from entity to DTO correctly")
        void mustMapNickNameFromEntityToDTOCorrectly() {
            assertThat(mapper.toDTO(baseEntity).getNickName()).isEqualTo("El Pibe");
        }

        @Test @DisplayName("Must map all fields correctly in a single conversion")
        void mustMapAllFieldsCorrectlyInSingleConversion() {
            AthleticProfileDTO result = mapper.toDTO(baseEntity);
            assertThat(result.getDorsalNumber()).isEqualTo(baseEntity.getDorsalNumber());
            assertThat(result.getPosition()).isEqualTo(baseEntity.getPosition());
            assertThat(result.getLaterality()).isEqualTo(baseEntity.getLaterality());
            assertThat(result.getStature()).isEqualTo(baseEntity.getStature());
            assertThat(result.getState()).isEqualTo(baseEntity.getState());
            assertThat(result.getNickName()).isEqualTo(baseEntity.getNickName());
        }
    }

    @Nested @DisplayName("toEntity - DTO to Entity conversion")
    class ToEntityTests {

        @Test @DisplayName("Must return a non-null entity when DTO is valid")
        void mustReturnNonNullEntityWhenDTOIsValid() {
            assertThat(mapper.toEntity(baseDTO)).isNotNull();
        }

        @Test @DisplayName("Must map dorsalNumber from DTO to entity correctly")
        void mustMapDorsalNumberFromDTOToEntityCorrectly() {
            assertThat(mapper.toEntity(baseDTO).getDorsalNumber()).isEqualTo(10);
        }

        @Test @DisplayName("Must map position from DTO to entity correctly")
        void mustMapPositionFromDTOToEntityCorrectly() {
            assertThat(mapper.toEntity(baseDTO).getPosition()).isEqualTo("FORWARD");
        }

        @Test @DisplayName("Must map laterality from DTO to entity correctly")
        void mustMapLateralityFromDTOToEntityCorrectly() {
            assertThat(mapper.toEntity(baseDTO).getLaterality()).isEqualTo("RIGHT");
        }

        @Test @DisplayName("Must map stature from DTO to entity correctly")
        void mustMapStatureFromDTOToEntityCorrectly() {
            assertThat(mapper.toEntity(baseDTO).getStature()).isEqualTo("180cm");
        }

        @Test @DisplayName("Must map state from DTO to entity correctly")
        void mustMapStateFromDTOToEntityCorrectly() {
            assertThat(mapper.toEntity(baseDTO).getState()).isEqualTo("ACTIVE");
        }

        @Test @DisplayName("Must map all fields correctly in a single conversion")
        void mustMapAllFieldsCorrectlyInSingleConversion() {
            AthleticProfileEntity result = mapper.toEntity(baseDTO);
            assertThat(result.getDorsalNumber()).isEqualTo(baseDTO.getDorsalNumber());
            assertThat(result.getPosition()).isEqualTo(baseDTO.getPosition());
            assertThat(result.getLaterality()).isEqualTo(baseDTO.getLaterality());
            assertThat(result.getStature()).isEqualTo(baseDTO.getStature());
            assertThat(result.getState()).isEqualTo(baseDTO.getState());
        }
    }

    @Nested @DisplayName("Round-trip conversion")
    class RoundTripTests {

        @Test @DisplayName("Must preserve all values when converting entity to DTO and back to entity")
        void mustPreserveAllValuesInRoundTripEntityToDTOToEntity() {
            AthleticProfileDTO dto = mapper.toDTO(baseEntity);
            AthleticProfileEntity result = mapper.toEntity(dto);
            assertThat(result.getDorsalNumber()).isEqualTo(baseEntity.getDorsalNumber());
            assertThat(result.getPosition()).isEqualTo(baseEntity.getPosition());
            assertThat(result.getLaterality()).isEqualTo(baseEntity.getLaterality());
            assertThat(result.getStature()).isEqualTo(baseEntity.getStature());
            assertThat(result.getState()).isEqualTo(baseEntity.getState());
        }

        @Test @DisplayName("Must preserve all values when converting DTO to entity and back to DTO")
        void mustPreserveAllValuesInRoundTripDTOToEntityToDTO() {
            AthleticProfileEntity entity = mapper.toEntity(baseDTO);
            AthleticProfileDTO result = mapper.toDTO(entity);
            assertThat(result.getDorsalNumber()).isEqualTo(baseDTO.getDorsalNumber());
            assertThat(result.getPosition()).isEqualTo(baseDTO.getPosition());
            assertThat(result.getLaterality()).isEqualTo(baseDTO.getLaterality());
            assertThat(result.getStature()).isEqualTo(baseDTO.getStature());
            assertThat(result.getState()).isEqualTo(baseDTO.getState());
        }
    }
}
