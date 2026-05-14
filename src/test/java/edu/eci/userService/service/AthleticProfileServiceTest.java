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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for AthleticProfileService.
 * Adapted to the actual production API (userId-based, not email-based).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AthleticProfileService - Unit Tests")
class AthleticProfileServiceTest {

    @Mock private AthleticProfileRepository athleticProfileRepository;
    @Mock private AthleticProfileMapper athleticProfileMapper;
    @InjectMocks private AthleticProfileService athleticProfileService;

    private AthleticProfileEntity baseEntity;
    private AthleticProfileDTO baseDTO;

    @BeforeEach
    void setUp() {
        baseEntity = new AthleticProfileEntity();
        baseEntity.setDorsalNumber(10);
        baseEntity.setPosition("FORWARD");
        baseEntity.setLaterality("RIGHT");
        baseEntity.setStature("180cm");
        baseEntity.setState("ACTIVE");

        baseDTO = new AthleticProfileDTO();
        baseDTO.setDorsalNumber(10);
        baseDTO.setPosition("FORWARD");
        baseDTO.setLaterality("RIGHT");
        baseDTO.setStature("180cm");
        baseDTO.setState("ACTIVE");
    }

    @Nested @DisplayName("getAllAthleticProfiles")
    class GetAllAthleticProfilesTests {

        @Test @DisplayName("Must return a list with all profiles when profiles exist")
        void mustReturnListWithAllProfilesWhenProfilesExist() {
            when(athleticProfileRepository.findAll()).thenReturn(List.of(baseEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            List<AthleticProfileDTO> result = athleticProfileService.getAllAthleticProfiles();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getDorsalNumber()).isEqualTo(10);
            verify(athleticProfileRepository).findAll();
        }

        @Test @DisplayName("Must return an empty list when no profiles exist")
        void mustReturnEmptyListWhenNoProfilesExist() {
            when(athleticProfileRepository.findAll()).thenReturn(List.of());
            assertThat(athleticProfileService.getAllAthleticProfiles()).isEmpty();
            verify(athleticProfileRepository).findAll();
        }

        @Test @DisplayName("Must call mapper toDTO for each entity returned by the repository")
        void mustCallMapperToDTOForEachEntity() {
            AthleticProfileEntity secondEntity = new AthleticProfileEntity();
            secondEntity.setPosition("DEFENDER");
            AthleticProfileDTO secondDTO = new AthleticProfileDTO();
            secondDTO.setPosition("DEFENDER");
            when(athleticProfileRepository.findAll()).thenReturn(List.of(baseEntity, secondEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            when(athleticProfileMapper.toDTO(secondEntity)).thenReturn(secondDTO);
            List<AthleticProfileDTO> result = athleticProfileService.getAllAthleticProfiles();
            assertThat(result).hasSize(2);
            verify(athleticProfileMapper).toDTO(baseEntity);
            verify(athleticProfileMapper).toDTO(secondEntity);
        }
    }

    @Nested @DisplayName("getAthleticProfilesByUserId")
    class GetAthleticProfilesByUserIdTests {

        @Test @DisplayName("Must return the DTO when a profile exists for the given userId")
        void mustReturnDTOWhenProfileExistsForUserId() {
            when(athleticProfileRepository.findById(1L)).thenReturn(Optional.of(baseEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            AthleticProfileDTO result = athleticProfileService.getAthleticProfilesByUserId(1L);
            assertThat(result).isNotNull();
            assertThat(result.getDorsalNumber()).isEqualTo(10);
            assertThat(result.getPosition()).isEqualTo("FORWARD");
        }

        @Test @DisplayName("Must call repository findById with the exact userId provided")
        void mustCallRepositoryFindByIdWithExactUserId() {
            when(athleticProfileRepository.findById(1L)).thenReturn(Optional.of(baseEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            athleticProfileService.getAthleticProfilesByUserId(1L);
            verify(athleticProfileRepository).findById(1L);
        }
    }

    @Nested @DisplayName("getAthleticProfileByPosition")
    class GetAthleticProfileByPositionTests {

        @Test @DisplayName("Must return profiles matching the given position")
        void mustReturnProfilesMatchingGivenPosition() {
            when(athleticProfileRepository.findByPosition("FORWARD")).thenReturn(List.of(baseEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            List<AthleticProfileDTO> result = athleticProfileService.getAthleticProfileByPosition("FORWARD");
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPosition()).isEqualTo("FORWARD");
        }

        @Test @DisplayName("Must return empty list when no profiles match the given position")
        void mustReturnEmptyListWhenNoProfilesMatchPosition() {
            when(athleticProfileRepository.findByPosition("GOALKEEPER")).thenReturn(List.of());
            assertThat(athleticProfileService.getAthleticProfileByPosition("GOALKEEPER")).isEmpty();
        }

        @Test @DisplayName("Must call mapper toDTO for each entity returned by findByPosition")
        void mustCallMapperToDTOForEachEntityByPosition() {
            AthleticProfileEntity second = new AthleticProfileEntity();
            second.setPosition("FORWARD");
            AthleticProfileDTO secondDTO = new AthleticProfileDTO();
            secondDTO.setPosition("FORWARD");
            when(athleticProfileRepository.findByPosition("FORWARD")).thenReturn(List.of(baseEntity, second));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            when(athleticProfileMapper.toDTO(second)).thenReturn(secondDTO);
            assertThat(athleticProfileService.getAthleticProfileByPosition("FORWARD")).hasSize(2);
            verify(athleticProfileMapper).toDTO(baseEntity);
            verify(athleticProfileMapper).toDTO(second);
        }
    }

    @Nested @DisplayName("getAthleticProfileByLaterality")
    class GetAthleticProfileByLateralityTests {

        @Test @DisplayName("Must return profiles matching the given laterality")
        void mustReturnProfilesMatchingGivenLaterality() {
            when(athleticProfileRepository.findByLaterality("RIGHT")).thenReturn(List.of(baseEntity));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            List<AthleticProfileDTO> result = athleticProfileService.getAthleticProfileByLaterality("RIGHT");
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getLaterality()).isEqualTo("RIGHT");
        }

        @Test @DisplayName("Must return empty list when no profiles match the given laterality")
        void mustReturnEmptyListWhenNoProfilesMatchLaterality() {
            when(athleticProfileRepository.findByLaterality("LEFT")).thenReturn(List.of());
            assertThat(athleticProfileService.getAthleticProfileByLaterality("LEFT")).isEmpty();
        }

        @Test @DisplayName("Must call mapper toDTO for each entity returned by findByLaterality")
        void mustCallMapperForEachEntityByLaterality() {
            AthleticProfileEntity second = new AthleticProfileEntity();
            second.setLaterality("RIGHT");
            AthleticProfileDTO secondDTO = new AthleticProfileDTO();
            secondDTO.setLaterality("RIGHT");
            when(athleticProfileRepository.findByLaterality("RIGHT")).thenReturn(List.of(baseEntity, second));
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            when(athleticProfileMapper.toDTO(second)).thenReturn(secondDTO);
            assertThat(athleticProfileService.getAthleticProfileByLaterality("RIGHT")).hasSize(2);
        }
    }

    @Nested @DisplayName("createAthleticProfile")
    class CreateAthleticProfileTests {

        @Test @DisplayName("Must return the created DTO after saving the entity")
        void mustReturnCreatedDTOAfterSaving() {
            when(athleticProfileMapper.toEntity(baseDTO)).thenReturn(baseEntity);
            when(athleticProfileRepository.save(baseEntity)).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            AthleticProfileDTO result = athleticProfileService.createAthleticProfile(baseDTO);
            assertThat(result).isNotNull();
            assertThat(result.getDorsalNumber()).isEqualTo(10);
        }

        @Test @DisplayName("Must call mapper toEntity with the received DTO")
        void mustCallMapperToEntityWithReceivedDTO() {
            when(athleticProfileMapper.toEntity(baseDTO)).thenReturn(baseEntity);
            when(athleticProfileRepository.save(baseEntity)).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            athleticProfileService.createAthleticProfile(baseDTO);
            verify(athleticProfileMapper).toEntity(baseDTO);
        }

        @Test @DisplayName("Must call repository save with the entity produced by the mapper")
        void mustCallRepositorySaveWithEntityFromMapper() {
            when(athleticProfileMapper.toEntity(baseDTO)).thenReturn(baseEntity);
            when(athleticProfileRepository.save(baseEntity)).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(baseEntity)).thenReturn(baseDTO);
            athleticProfileService.createAthleticProfile(baseDTO);
            verify(athleticProfileRepository).save(baseEntity);
        }

        @Test @DisplayName("Must persist all fields from the DTO through entity and back to DTO")
        void mustPersistAllFieldsFromDTOToEntity() {
            AthleticProfileDTO fullDTO = new AthleticProfileDTO();
            fullDTO.setDorsalNumber(7);
            fullDTO.setPosition("DEFENDER");
            fullDTO.setLaterality("LEFT");
            fullDTO.setStature("175cm");
            fullDTO.setState("ACTIVE");

            AthleticProfileEntity fullEntity = new AthleticProfileEntity();
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
        }
    }

    @Nested @DisplayName("updateAthleticProfile")
    class UpdateAthleticProfileTests {

        @Test @DisplayName("Must update all fields and return the updated DTO")
        void mustUpdateAllFieldsAndReturnUpdatedDTO() {
            AthleticProfileDTO updatedDTO = new AthleticProfileDTO();
            updatedDTO.setDorsalNumber(11);
            updatedDTO.setPosition("MIDFIELDER");
            updatedDTO.setLaterality("LEFT");
            updatedDTO.setStature("175cm");
            updatedDTO.setState("INACTIVE");

            when(athleticProfileRepository.findById(1L)).thenReturn(Optional.of(baseEntity));
            when(athleticProfileRepository.save(any(AthleticProfileEntity.class))).thenReturn(baseEntity);
            when(athleticProfileMapper.toDTO(any())).thenReturn(updatedDTO);

            AthleticProfileDTO result = athleticProfileService.updateAthleticProfile(1L, updatedDTO);
            assertThat(result.getDorsalNumber()).isEqualTo(11);
            assertThat(result.getPosition()).isEqualTo("MIDFIELDER");
        }

        @Test @DisplayName("Must throw NoSuchElementException when no profile exists for the given userId")
        void mustThrowNoSuchElementExceptionWhenProfileNotFound() {
            when(athleticProfileRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> athleticProfileService.updateAthleticProfile(99L, baseDTO))
                .isInstanceOf(NoSuchElementException.class);
        }

        @Test @DisplayName("Must not call repository save when profile is not found")
        void mustNotCallRepositorySaveWhenProfileNotFound() {
            when(athleticProfileRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> athleticProfileService.updateAthleticProfile(99L, baseDTO))
                .isInstanceOf(NoSuchElementException.class);
            verify(athleticProfileRepository, never()).save(any());
        }

        @Test @DisplayName("Must update dorsalNumber on the existing entity before saving")
        void mustUpdateDorsalNumberOnExistingEntityBeforeSaving() {
            AthleticProfileDTO updatedDTO = new AthleticProfileDTO();
            updatedDTO.setDorsalNumber(99);
            updatedDTO.setPosition("FORWARD");
            updatedDTO.setLaterality("RIGHT");
            updatedDTO.setStature("180cm");
            updatedDTO.setState("ACTIVE");
            when(athleticProfileRepository.findById(1L)).thenReturn(Optional.of(baseEntity));
            when(athleticProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(athleticProfileMapper.toDTO(any())).thenReturn(updatedDTO);
            athleticProfileService.updateAthleticProfile(1L, updatedDTO);
            assertThat(baseEntity.getDorsalNumber()).isEqualTo(99);
        }
    }

    @Nested @DisplayName("deleteAthleticProfile")
    class DeleteAthleticProfileTests {

        @Test @DisplayName("Must call deleteById when profile exists")
        void mustCallDeleteByIdWhenProfileExists() {
            when(athleticProfileRepository.existsById(1L)).thenReturn(true);
            athleticProfileService.deleteAthleticProfile(1L);
            verify(athleticProfileRepository).deleteById(1L);
        }

        @Test @DisplayName("Must throw IllegalArgumentException when profile does not exist")
        void mustThrowIllegalArgumentExceptionWhenProfileDoesNotExist() {
            when(athleticProfileRepository.existsById(99L)).thenReturn(false);
            assertThatThrownBy(() -> athleticProfileService.deleteAthleticProfile(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Athletic profile not found");
        }

        @Test @DisplayName("Must not call deleteById when profile does not exist")
        void mustNotCallDeleteByIdWhenProfileDoesNotExist() {
            when(athleticProfileRepository.existsById(99L)).thenReturn(false);
            assertThatThrownBy(() -> athleticProfileService.deleteAthleticProfile(99L))
                .isInstanceOf(IllegalArgumentException.class);
            verify(athleticProfileRepository, never()).deleteById(any());
        }

        @Test @DisplayName("Must call existsById before attempting deletion")
        void mustCallExistsByIdBeforeDeletion() {
            when(athleticProfileRepository.existsById(1L)).thenReturn(true);
            athleticProfileService.deleteAthleticProfile(1L);
            verify(athleticProfileRepository).existsById(1L);
        }
    }
}
