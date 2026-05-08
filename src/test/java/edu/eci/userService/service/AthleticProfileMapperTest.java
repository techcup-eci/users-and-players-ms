package edu.eci.userService.service;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.mappers.AthleticProfileMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * Unit tests for AthleticProfileMapper.
 *
 * No mocks needed. The mapper is tested directly since it has no dependencies.
 * Covers both toDTO and toEntity conversion methods completely.
 *
 * Pattern: AAA (Arrange - Act - Assert)
 * Framework: JUnit 5
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
        baseEntity.setEmail("player@escuela.edu.co");
        baseEntity.setDorsalNumber(10);
        baseEntity.setPosition("FORWARD");
        baseEntity.setLaterality("RIGHT");
        baseEntity.setStature("180cm");
        baseEntity.setState("ACTIVE");

        baseDTO = new AthleticProfileDTO(10, "player@escuela.edu.co", "FORWARD", "RIGHT", "180cm", "ACTIVE");
    }

    // ----------------------------------------------------------------
    // toDTO
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("toDTO - Entity to DTO conversion")
    class ToDTOTests {

        @Test
        @DisplayName("Must return a non-null DTO when entity is valid")
        void mustReturnNonNullDTOWhenEntityIsValid() {
            AthleticProfileDTO result = mapper.toDTO(baseEntity);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Must map email from entity to DTO correctly")
        void mustMapEmailFromEntityToDTOCorrectly() {
            AthleticProfileDTO result = mapper.toDTO(baseEntity);

            assertThat(result.getEmail()).isEqualTo("player@escuela.edu.co");
        }

        @Test
        @DisplayName("Must map dorsalNumber from entity to DTO correctly")
        void mustMapDorsalNumberFromEntityToDTOCorrectly() {
            AthleticProfileDTO result = mapper.toDTO(baseEntity);

            assertThat(result.getDorsalNumber()).isEqualTo(10);
        }

        @Test
        @DisplayName("Must map position from entity to DTO correctly")
        void mustMapPositionFromEntityToDTOCorrectly() {
            AthleticProfileDTO result = mapper.toDTO(baseEntity);

            assertThat(result.getPosition()).isEqualTo("FORWARD");
        }

        @Test
        @DisplayName("Must map laterality from entity to DTO correctly")
        void mustMapLateralityFromEntityToDTOCorrectly() {
            AthleticProfileDTO result = mapper.toDTO(baseEntity);

            assertThat(result.getLaterality()).isEqualTo("RIGHT");
        }

        @Test
        @DisplayName("Must map stature from entity to DTO correctly")
        void mustMapStatureFromEntityToDTOCorrectly() {
            AthleticProfileDTO result = mapper.toDTO(baseEntity);

            assertThat(result.getStature()).isEqualTo("180cm");
        }

        @Test
        @DisplayName("Must map state from entity to DTO correctly")
        void mustMapStateFromEntityToDTOCorrectly() {
            AthleticProfileDTO result = mapper.toDTO(baseEntity);

            assertThat(result.getState()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("Must map all fields correctly in a single conversion")
        void mustMapAllFieldsCorrectlyInSingleConversion() {
            AthleticProfileDTO result = mapper.toDTO(baseEntity);

            assertThat(result.getEmail()).isEqualTo(baseEntity.getEmail());
            assertThat(result.getDorsalNumber()).isEqualTo(baseEntity.getDorsalNumber());
            assertThat(result.getPosition()).isEqualTo(baseEntity.getPosition());
            assertThat(result.getLaterality()).isEqualTo(baseEntity.getLaterality());
            assertThat(result.getStature()).isEqualTo(baseEntity.getStature());
            assertThat(result.getState()).isEqualTo(baseEntity.getState());
        }
    }

    // ----------------------------------------------------------------
    // toEntity
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("toEntity - DTO to Entity conversion")
    class ToEntityTests {

        @Test
        @DisplayName("Must return a non-null entity when DTO is valid")
        void mustReturnNonNullEntityWhenDTOIsValid() {
            AthleticProfileEntity result = mapper.toEntity(baseDTO);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Must map email from DTO to entity correctly")
        void mustMapEmailFromDTOToEntityCorrectly() {
            AthleticProfileEntity result = mapper.toEntity(baseDTO);

            assertThat(result.getEmail()).isEqualTo("player@escuela.edu.co");
        }

        @Test
        @DisplayName("Must map dorsalNumber from DTO to entity correctly")
        void mustMapDorsalNumberFromDTOToEntityCorrectly() {
            AthleticProfileEntity result = mapper.toEntity(baseDTO);

            assertThat(result.getDorsalNumber()).isEqualTo(10);
        }

        @Test
        @DisplayName("Must map position from DTO to entity correctly")
        void mustMapPositionFromDTOToEntityCorrectly() {
            AthleticProfileEntity result = mapper.toEntity(baseDTO);

            assertThat(result.getPosition()).isEqualTo("FORWARD");
        }

        @Test
        @DisplayName("Must map laterality from DTO to entity correctly")
        void mustMapLateralityFromDTOToEntityCorrectly() {
            AthleticProfileEntity result = mapper.toEntity(baseDTO);

            assertThat(result.getLaterality()).isEqualTo("RIGHT");
        }

        @Test
        @DisplayName("Must map stature from DTO to entity correctly")
        void mustMapStatureFromDTOToEntityCorrectly() {
            AthleticProfileEntity result = mapper.toEntity(baseDTO);

            assertThat(result.getStature()).isEqualTo("180cm");
        }

        @Test
        @DisplayName("Must map state from DTO to entity correctly")
        void mustMapStateFromDTOToEntityCorrectly() {
            AthleticProfileEntity result = mapper.toEntity(baseDTO);

            assertThat(result.getState()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("Must map all fields correctly in a single conversion")
        void mustMapAllFieldsCorrectlyInSingleConversion() {
            AthleticProfileEntity result = mapper.toEntity(baseDTO);

            assertThat(result.getEmail()).isEqualTo(baseDTO.getEmail());
            assertThat(result.getDorsalNumber()).isEqualTo(baseDTO.getDorsalNumber());
            assertThat(result.getPosition()).isEqualTo(baseDTO.getPosition());
            assertThat(result.getLaterality()).isEqualTo(baseDTO.getLaterality());
            assertThat(result.getStature()).isEqualTo(baseDTO.getStature());
            assertThat(result.getState()).isEqualTo(baseDTO.getState());
        }
    }

    // ----------------------------------------------------------------
    // Round-trip: entity -> DTO -> entity
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Round-trip conversion")
    class RoundTripTests {

        @Test
        @DisplayName("Must preserve all values when converting entity to DTO and back to entity")
        void mustPreserveAllValuesInRoundTripEntityToDTOToEntity() {
            AthleticProfileDTO dto = mapper.toDTO(baseEntity);
            AthleticProfileEntity result = mapper.toEntity(dto);

            assertThat(result.getEmail()).isEqualTo(baseEntity.getEmail());
            assertThat(result.getDorsalNumber()).isEqualTo(baseEntity.getDorsalNumber());
            assertThat(result.getPosition()).isEqualTo(baseEntity.getPosition());
            assertThat(result.getLaterality()).isEqualTo(baseEntity.getLaterality());
            assertThat(result.getStature()).isEqualTo(baseEntity.getStature());
            assertThat(result.getState()).isEqualTo(baseEntity.getState());
        }

        @Test
        @DisplayName("Must preserve all values when converting DTO to entity and back to DTO")
        void mustPreserveAllValuesInRoundTripDTOToEntityToDTO() {
            AthleticProfileEntity entity = mapper.toEntity(baseDTO);
            AthleticProfileDTO result = mapper.toDTO(entity);

            assertThat(result.getEmail()).isEqualTo(baseDTO.getEmail());
            assertThat(result.getDorsalNumber()).isEqualTo(baseDTO.getDorsalNumber());
            assertThat(result.getPosition()).isEqualTo(baseDTO.getPosition());
            assertThat(result.getLaterality()).isEqualTo(baseDTO.getLaterality());
            assertThat(result.getStature()).isEqualTo(baseDTO.getStature());
            assertThat(result.getState()).isEqualTo(baseDTO.getState());
        }
    }
}
