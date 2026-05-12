package edu.eci.userService.service;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.mappers.AthleticProfileMapper;
import edu.eci.userService.repository.AthleticProfileRepository;
import edu.eci.userService.services.AthleticProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit tests for AthleticProfileService.
 *
 * All dependencies (repository and mapper) are mocked with Mockito.
 * Tests are written against the real production classes:
 *   - AthleticProfileService
 *   - AthleticProfileRepository
 *   - AthleticProfileMapper
 *   - AthleticProfileEntity
 *   - AthleticProfileDTO
 *
 * Pattern: AAA (Arrange - Act - Assert)
 * Framework: JUnit 5 + Mockito
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AthleticProfileService - Unit Tests")
class AthleticProfileServiceTest {

    @Mock
    private AthleticProfileRepository athleticProfileRepository;

    @Mock
    private AthleticProfileMapper athleticProfileMapper;

    @InjectMocks
    private AthleticProfileService athleticProfileService;

    private AthleticProfileEntity baseEntity;
    private AthleticProfileDTO baseDTO;

    @BeforeEach
    void setUp() {
        baseEntity = new AthleticProfileEntity();
        baseEntity.setEmail("player@escuela.edu.co");
        baseEntity.setDorsalNumber(10);
        baseEntity.setPosition("FORWARD");
        baseEntity.setLaterality("RIGHT");
        baseEntity.setStature("180cm");
        baseEntity.setState("ACTIVE");

        baseDTO = new AthleticProfileDTO();
        baseDTO.setEmail("player@escuela.edu.co");
        baseDTO.setDorsalNumber(10);
        baseDTO.setPosition("FORWARD");
        baseDTO.setLaterality("RIGHT");
        baseDTO.setStature("180cm");
        baseDTO.setState("ACTIVE");
    }

    // ----------------------------------------------------------------
    // getAllAthleticProfiles
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("getAllAthleticProfiles")
    class GetAllAthleticProfilesTests {

        @Test
        @DisplayName("Must return a list with all profiles when profiles exist")
        void mustReturnListWithAllProfilesWhenProfilesExist() {
            when(athleticProfileRepository.findAll()).thenReturn(List.of(baseEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            List<AthleticProfileDTO> result = athleticProfileService.getAllAthleticProfiles();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getEmail()).isEqualTo("player@escuela.edu.co");
            verify(athleticProfileRepository).findAll();
        }

        @Test
        @DisplayName("Must return an empty list when no profiles exist")
        void mustReturnEmptyListWhenNoProfilesExist() {
            when(athleticProfileRepository.findAll()).thenReturn(List.of());

            List<AthleticProfileDTO> result = athleticProfileService.getAllAthleticProfiles();

            assertThat(result).isEmpty();
            verify(athleticProfileRepository).findAll();
        }

        @Test
        @DisplayName("Must call mapper toDTO for each entity returned by the repository")
        void mustCallMapperToDTOForEachEntity() {
            AthleticProfileEntity secondEntity = new AthleticProfileEntity();
            secondEntity.setEmail("second@escuela.edu.co");
            AthleticProfileDTO secondDTO = new AthleticProfileDTO();
            secondDTO.setEmail("second@escuela.edu.co");

            when(athleticProfileRepository.findAll()).thenReturn(List.of(baseEntity, secondEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            when(athleticProfileMapper.toDTO(secondEntity)).thenReturn(secondDTO);

            List<AthleticProfileDTO> result = athleticProfileService.getAllAthleticProfiles();

            assertThat(result).hasSize(2);
            verify(athleticProfileMapper).toDTO(baseEntity);
            verify(athleticProfileMapper).toDTO(secondEntity);
        }
    }

    // ----------------------------------------------------------------
    // getAthleticProfilesByEmail
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("getAthleticProfilesByEmail")
    class GetAthleticProfilesByEmailTests {

        @Test
        @DisplayName("Must return the DTO when a profile exists for the given email")
        void mustReturnDTOWhenProfileExistsForEmail() {
            when(athleticProfileRepository.findByEmail("player@escuela.edu.co")).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            AthleticProfileDTO result = athleticProfileService.getAthleticProfilesByEmail("player@escuela.edu.co");

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("player@escuela.edu.co");
            assertThat(result.getDorsalNumber()).isEqualTo(10);
            assertThat(result.getPosition()).isEqualTo("FORWARD");
        }

        @Test
        @DisplayName("Must call repository findByEmail with the exact email provided")
        void mustCallRepositoryFindByEmailWithExactEmail() {
            when(athleticProfileRepository.findByEmail("player@escuela.edu.co")).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            athleticProfileService.getAthleticProfilesByEmail("player@escuela.edu.co");

            verify(athleticProfileRepository).findByEmail("player@escuela.edu.co");
        }

        @Test
        @DisplayName("Must call mapper toDTO with the entity returned by the repository")
        void mustCallMapperToDTOWithEntityFromRepository() {
            when(athleticProfileRepository.findByEmail("player@escuela.edu.co")).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            athleticProfileService.getAthleticProfilesByEmail("player@escuela.edu.co");

            verify(athleticProfileMapper).toDTO(baseEntity);
        }
    }

    // ----------------------------------------------------------------
    // getAthleticProfileByPosition
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("getAthleticProfileByPosition")
    class GetAthleticProfileByPositionTests {

        @Test
        @DisplayName("Must return profiles matching the given position")
        void mustReturnProfilesMatchingGivenPosition() {
            when(athleticProfileRepository.findByPosition("FORWARD")).thenReturn(List.of(baseEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            List<AthleticProfileDTO> result = athleticProfileService.getAthleticProfileByPosition("FORWARD");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPosition()).isEqualTo("FORWARD");
        }

        @Test
        @DisplayName("Must return empty list when no profiles match the given position")
        void mustReturnEmptyListWhenNoProfilesMatchPosition() {
            when(athleticProfileRepository.findByPosition("GOALKEEPER")).thenReturn(List.of());

            List<AthleticProfileDTO> result = athleticProfileService.getAthleticProfileByPosition("GOALKEEPER");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Must call mapper toDTO for each entity returned by findByPosition")
        void mustCallMapperToDTOForEachEntityByPosition() {
            AthleticProfileEntity second = new AthleticProfileEntity();
            second.setPosition("FORWARD");
            AthleticProfileDTO secondDTO = new AthleticProfileDTO();
            secondDTO.setPosition("FORWARD");

            when(athleticProfileRepository.findByPosition("FORWARD")).thenReturn(List.of(baseEntity, second));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            when(athleticProfileMapper.toDTO(second)).thenReturn(secondDTO);

            List<AthleticProfileDTO> result = athleticProfileService.getAthleticProfileByPosition("FORWARD");

            assertThat(result).hasSize(2);
            verify(athleticProfileMapper).toDTO(baseEntity);
            verify(athleticProfileMapper).toDTO(second);
        }
    }

    // ----------------------------------------------------------------
    // getAthleticProfileByLaterality
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("getAthleticProfileByLaterality")
    class GetAthleticProfileByLateralityTests {

        @Test
        @DisplayName("Must return profiles matching the given laterality")
        void mustReturnProfilesMatchingGivenLaterality() {
            when(athleticProfileRepository.findByLaterality("RIGHT")).thenReturn(List.of(baseEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            List<AthleticProfileDTO> result = athleticProfileService.getAthleticProfileByLaterality("RIGHT");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getLaterality()).isEqualTo("RIGHT");
        }

        @Test
        @DisplayName("Must return empty list when no profiles match the given laterality")
        void mustReturnEmptyListWhenNoProfilesMatchLaterality() {
            when(athleticProfileRepository.findByLaterality("LEFT")).thenReturn(List.of());

            List<AthleticProfileDTO> result = athleticProfileService.getAthleticProfileByLaterality("LEFT");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Must call mapper toDTO for each entity returned by findByLaterality")
        void mustCallMapperForEachEntityByLaterality() {
            AthleticProfileEntity second = new AthleticProfileEntity();
            second.setLaterality("RIGHT");
            AthleticProfileDTO secondDTO = new AthleticProfileDTO();
            secondDTO.setLaterality("RIGHT");

            when(athleticProfileRepository.findByLaterality("RIGHT")).thenReturn(List.of(baseEntity, second));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            when(athleticProfileMapper.toDTO(second)).thenReturn(secondDTO);

            List<AthleticProfileDTO> result = athleticProfileService.getAthleticProfileByLaterality("RIGHT");

            assertThat(result).hasSize(2);
        }
    }

    // ----------------------------------------------------------------
    // createAthleticProfile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("createAthleticProfile")
    class CreateAthleticProfileTests {

        @Test
        @DisplayName("Must return the created DTO after saving the entity")
        void mustReturnCreatedDTOAfterSaving() {
            when(athleticProfileMapper.toEntity(baseDTO)).thenReturn(baseEntity);
            when(athleticProfileRepository.save(baseEntity)).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            AthleticProfileDTO result = athleticProfileService.createAthleticProfile(baseDTO);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("player@escuela.edu.co");
            assertThat(result.getDorsalNumber()).isEqualTo(10);
        }

        @Test
        @DisplayName("Must call mapper toEntity with the received DTO")
        void mustCallMapperToEntityWithReceivedDTO() {
            when(athleticProfileMapper.toEntity(baseDTO)).thenReturn(baseEntity);
            when(athleticProfileRepository.save(baseEntity)).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            athleticProfileService.createAthleticProfile(baseDTO);

            verify(athleticProfileMapper).toEntity(baseDTO);
        }

        @Test
        @DisplayName("Must call repository save with the entity produced by the mapper")
        void mustCallRepositorySaveWithEntityFromMapper() {
            when(athleticProfileMapper.toEntity(baseDTO)).thenReturn(baseEntity);
            when(athleticProfileRepository.save(baseEntity)).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            athleticProfileService.createAthleticProfile(baseDTO);

            verify(athleticProfileRepository).save(baseEntity);
        }

        @Test
        @DisplayName("Must call mapper toDTO with the entity returned by repository save")
        void mustCallMapperToDTOWithSavedEntity() {
            when(athleticProfileMapper.toEntity(baseDTO)).thenReturn(baseEntity);
            when(athleticProfileRepository.save(baseEntity)).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);

            athleticProfileService.createAthleticProfile(baseDTO);

            verify(athleticProfileMapper).toDTO(baseEntity);
        }

        @Test
        @DisplayName("Must persist all fields from the DTO through entity and back to DTO")
        void mustPersistAllFieldsFromDTOToEntity() {
            AthleticProfileDTO fullDTO = new AthleticProfileDTO(7, "new@escuela.edu.co", "DEFENDER", "LEFT", "175cm", "ACTIVE");
            AthleticProfileEntity fullEntity = new AthleticProfileEntity();
            fullEntity.setEmail("new@escuela.edu.co");
            fullEntity.setDorsalNumber(7);
            fullEntity.setPosition("DEFENDER");
            fullEntity.setLaterality("LEFT");
            fullEntity.setStature("175cm");
            fullEntity.setState("ACTIVE");

            when(athleticProfileMapper.toEntity(fullDTO)).thenReturn(fullEntity);
            when(athleticProfileRepository.save(fullEntity)).thenReturn(fullEntity);
            when(athleticProfileMapper.toDTO(fullEntity)).thenReturn(fullDTO);

            AthleticProfileDTO result = athleticProfileService.createAthleticProfile(fullDTO);

            assertThat(result.getDorsalNumber()).isEqualTo(7);
            assertThat(result.getPosition()).isEqualTo("DEFENDER");
            assertThat(result.getLaterality()).isEqualTo("LEFT");
            assertThat(result.getStature()).isEqualTo("175cm");
            assertThat(result.getState()).isEqualTo("ACTIVE");
        }
    }

    // ----------------------------------------------------------------
    // updateAthleticProfile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("updateAthleticProfile")
    class UpdateAthleticProfileTests {

        @Test
        @DisplayName("Must update all fields and return the updated DTO")
        void mustUpdateAllFieldsAndReturnUpdatedDTO() {
            AthleticProfileDTO updatedDTO = new AthleticProfileDTO();
            updatedDTO.setDorsalNumber(11);
            updatedDTO.setPosition("MIDFIELDER");
            updatedDTO.setLaterality("LEFT");
            updatedDTO.setStature("175cm");
            updatedDTO.setState("INACTIVE");

            AthleticProfileEntity updatedEntity = new AthleticProfileEntity();
            updatedEntity.setEmail("player@escuela.edu.co");
            updatedEntity.setDorsalNumber(11);
            updatedEntity.setPosition("MIDFIELDER");
            updatedEntity.setLaterality("LEFT");
            updatedEntity.setStature("175cm");
            updatedEntity.setState("INACTIVE");

            when(athleticProfileRepository.findByEmail("player@escuela.edu.co")).thenReturn(baseEntity);
            when(athleticProfileRepository.save(any(AthleticProfileEntity.class))).thenReturn(updatedEntity);
            when(athleticProfileMapper.toDTO(updatedEntity)).thenReturn(updatedDTO);

            AthleticProfileDTO result = athleticProfileService.updateAthleticProfile(
                "player@escuela.edu.co", updatedDTO
            );

            assertThat(result.getDorsalNumber()).isEqualTo(11);
            assertThat(result.getPosition()).isEqualTo("MIDFIELDER");
            assertThat(result.getLaterality()).isEqualTo("LEFT");
            assertThat(result.getStature()).isEqualTo("175cm");
            assertThat(result.getState()).isEqualTo("INACTIVE");
        }

        @Test
        @DisplayName("Must call repository save after updating the entity fields")
        void mustCallRepositorySaveAfterUpdatingFields() {
            AthleticProfileDTO updatedDTO = new AthleticProfileDTO();
            updatedDTO.setDorsalNumber(11);
            updatedDTO.setPosition("MIDFIELDER");
            updatedDTO.setLaterality("LEFT");
            updatedDTO.setStature("175cm");
            updatedDTO.setState("INACTIVE");

            when(athleticProfileRepository.findByEmail("player@escuela.edu.co")).thenReturn(baseEntity);
            when(athleticProfileRepository.save(any(AthleticProfileEntity.class))).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(any())).thenReturn(updatedDTO);

            athleticProfileService.updateAthleticProfile("player@escuela.edu.co", updatedDTO);

            verify(athleticProfileRepository).save(baseEntity);
        }

        @Test
        @DisplayName("Must throw NoSuchElementException when no profile exists for the given email")
        void mustThrowNoSuchElementExceptionWhenProfileNotFound() {
            when(athleticProfileRepository.findByEmail("unknown@escuela.edu.co")).thenReturn(null);

            assertThatThrownBy(() ->
                athleticProfileService.updateAthleticProfile("unknown@escuela.edu.co", baseDTO)
            ).isInstanceOf(NoSuchElementException.class)
             .hasMessageContaining("unknown@escuela.edu.co");
        }

        @Test
        @DisplayName("Must not call repository save when profile is not found")
        void mustNotCallRepositorySaveWhenProfileNotFound() {
            when(athleticProfileRepository.findByEmail("unknown@escuela.edu.co")).thenReturn(null);

            assertThatThrownBy(() ->
                athleticProfileService.updateAthleticProfile("unknown@escuela.edu.co", baseDTO)
            ).isInstanceOf(NoSuchElementException.class);

            verify(athleticProfileRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must update dorsalNumber on the existing entity before saving")
        void mustUpdateDorsalNumberOnExistingEntityBeforeSaving() {
            AthleticProfileDTO updatedDTO = new AthleticProfileDTO();
            updatedDTO.setDorsalNumber(99);
            updatedDTO.setPosition("FORWARD");
            updatedDTO.setLaterality("RIGHT");
            updatedDTO.setStature("180cm");
            updatedDTO.setState("ACTIVE");

            when(athleticProfileRepository.findByEmail("player@escuela.edu.co")).thenReturn(baseEntity);
            when(athleticProfileRepository.save(any(AthleticProfileEntity.class))).thenAnswer(inv -> inv.getArgument(0));
            when(athleticProfileMapper.toDTO(any())).thenReturn(updatedDTO);

            athleticProfileService.updateAthleticProfile("player@escuela.edu.co", updatedDTO);

            assertThat(baseEntity.getDorsalNumber()).isEqualTo(99);
        }
    }

    // ----------------------------------------------------------------
    // deleteAthleticProfile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("deleteAthleticProfile")
    class DeleteAthleticProfileTests {

        @Test
        @DisplayName("Must call deleteByEmail when profile exists")
        void mustCallDeleteByEmailWhenProfileExists() {
            when(athleticProfileRepository.existsByEmail("player@escuela.edu.co")).thenReturn(true);

            athleticProfileService.deleteAthleticProfile("player@escuela.edu.co");

            verify(athleticProfileRepository).deleteByEmail("player@escuela.edu.co");
        }

        @Test
        @DisplayName("Must throw IllegalArgumentException when profile does not exist")
        void mustThrowIllegalArgumentExceptionWhenProfileDoesNotExist() {
            when(athleticProfileRepository.existsByEmail("unknown@escuela.edu.co")).thenReturn(false);

            assertThatThrownBy(() ->
                athleticProfileService.deleteAthleticProfile("unknown@escuela.edu.co")
            ).isInstanceOf(IllegalArgumentException.class)
             .hasMessageContaining("Athletic profile not found");
        }

        @Test
        @DisplayName("Must not call deleteByEmail when profile does not exist")
        void mustNotCallDeleteByEmailWhenProfileDoesNotExist() {
            when(athleticProfileRepository.existsByEmail("unknown@escuela.edu.co")).thenReturn(false);

            assertThatThrownBy(() ->
                athleticProfileService.deleteAthleticProfile("unknown@escuela.edu.co")
            ).isInstanceOf(IllegalArgumentException.class);

            verify(athleticProfileRepository, never()).deleteByEmail(any());
        }

        @Test
        @DisplayName("Must call existsByEmail before attempting deletion")
        void mustCallExistsByEmailBeforeDeletion() {
            when(athleticProfileRepository.existsByEmail("player@escuela.edu.co")).thenReturn(true);

            athleticProfileService.deleteAthleticProfile("player@escuela.edu.co");

            verify(athleticProfileRepository).existsByEmail("player@escuela.edu.co");
        }
    }
}
