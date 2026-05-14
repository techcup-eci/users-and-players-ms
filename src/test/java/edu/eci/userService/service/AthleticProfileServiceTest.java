package edu.eci.userService.service;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.entities.UserEntity;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para {@link AthleticProfileService}.
 */
@ExtendWith(MockitoExtension.class)
class AthleticProfileServiceTest {

    @Mock
    private AthleticProfileRepository athleticProfileRepository;

    @Mock
    private AthleticProfileMapper athleticProfileMapper;

    @InjectMocks
    private AthleticProfileService athleticProfileService;

    // ── Fixtures ─────────────────────────────────────────────────────────────

    private AthleticProfileEntity sampleEntity;
    private AthleticProfileDTO sampleDTO;
    private UserEntity sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new UserEntity();
        sampleUser.setId(1L);
        sampleUser.setName("Juan Pérez");
        sampleUser.setEmail("juan.perez@eci.edu.co");

        sampleEntity = new AthleticProfileEntity();
        sampleEntity.setId(1L);
        sampleEntity.setDorsalNumber(10);
        sampleEntity.setNickName("Juancho");
        sampleEntity.setPosition("delantero");
        sampleEntity.setLaterality("diestro");
        sampleEntity.setStature("175cm");
        sampleEntity.setState("activo");
        sampleEntity.setUser(sampleUser);

        sampleDTO = new AthleticProfileDTO();
        sampleDTO.setId(1L);
        sampleDTO.setDorsalNumber(10);
        sampleDTO.setNickName("Juancho");
        sampleDTO.setPosition("delantero");
        sampleDTO.setLaterality("diestro");
        sampleDTO.setStature("175cm");
        sampleDTO.setState("activo");
        sampleDTO.setUser(sampleUser);
    }

    // ── getAllAthleticProfiles ────────────────────────────────────────────────

    @Nested
    @DisplayName("getAllAthleticProfiles()")
    class GetAll {

        @Test
        @DisplayName("Debe retornar lista de DTOs cuando existen perfiles")
        void shouldReturnDTOList() {
            when(athleticProfileRepository.findAll()).thenReturn(List.of(sampleEntity));
            when(athleticProfileMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

            List<AthleticProfileDTO> result = athleticProfileService.getAllAthleticProfiles();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getNickName()).isEqualTo("Juancho");
            verify(athleticProfileRepository).findAll();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay perfiles")
        void shouldReturnEmptyList() {
            when(athleticProfileRepository.findAll()).thenReturn(List.of());

            List<AthleticProfileDTO> result = athleticProfileService.getAllAthleticProfiles();

            assertThat(result).isEmpty();
        }
    }

    // ── getAthleticProfilesByUserId ──────────────────────────────────────────

    @Nested
    @DisplayName("getAthleticProfilesByUserId()")
    class GetById {

        @Test
        @DisplayName("Debe retornar DTO cuando el perfil existe")
        void shouldReturnDTOWhenFound() {
            when(athleticProfileRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(athleticProfileMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

            AthleticProfileDTO result = athleticProfileService.getAthleticProfilesByUserId(1L);

            assertThat(result).isNotNull();
            assertThat(result.getPosition()).isEqualTo("delantero");
        }

        @Test
        @DisplayName("Debe retornar null cuando el perfil no existe")
        void shouldReturnNullWhenNotFound() {
            when(athleticProfileRepository.findById(99L)).thenReturn(Optional.empty());
            when(athleticProfileMapper.toDTO(null)).thenReturn(null);

            AthleticProfileDTO result = athleticProfileService.getAthleticProfilesByUserId(99L);

            assertThat(result).isNull();
        }
    }

    // ── getAthleticProfileByPosition ─────────────────────────────────────────

    @Nested
    @DisplayName("getAthleticProfileByPosition()")
    class GetByPosition {

        @Test
        @DisplayName("Debe retornar lista de perfiles con la posición indicada")
        void shouldReturnProfilesForPosition() {
            when(athleticProfileRepository.findByPosition("delantero"))
                    .thenReturn(List.of(sampleEntity));
            when(athleticProfileMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

            List<AthleticProfileDTO> result =
                    athleticProfileService.getAthleticProfileByPosition("delantero");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPosition()).isEqualTo("delantero");
        }

        @Test
        @DisplayName("Debe retornar lista vacía si no hay perfiles para esa posición")
        void shouldReturnEmptyListWhenNoMatch() {
            when(athleticProfileRepository.findByPosition("portero")).thenReturn(List.of());

            List<AthleticProfileDTO> result =
                    athleticProfileService.getAthleticProfileByPosition("portero");

            assertThat(result).isEmpty();
        }
    }

    // ── getAthleticProfileByLaterality ───────────────────────────────────────

    @Nested
    @DisplayName("getAthleticProfileByLaterality()")
    class GetByLaterality {

        @Test
        @DisplayName("Debe retornar perfiles con la lateralidad indicada")
        void shouldReturnProfilesForLaterality() {
            when(athleticProfileRepository.findByLaterality("diestro"))
                    .thenReturn(List.of(sampleEntity));
            when(athleticProfileMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

            List<AthleticProfileDTO> result =
                    athleticProfileService.getAthleticProfileByLaterality("diestro");

            assertThat(result).hasSize(1);
        }
    }

    // ── createAthleticProfile ────────────────────────────────────────────────

    @Nested
    @DisplayName("createAthleticProfile()")
    class Create {

        @Test
        @DisplayName("Debe persistir y retornar el DTO del perfil creado")
        void shouldPersistAndReturnDTO() {
            when(athleticProfileMapper.toEntity(sampleDTO)).thenReturn(sampleEntity);
            when(athleticProfileRepository.save(sampleEntity)).thenReturn(sampleEntity);
            when(athleticProfileMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

            AthleticProfileDTO result = athleticProfileService.createAthleticProfile(sampleDTO);

            assertThat(result).isNotNull();
            assertThat(result.getDorsalNumber()).isEqualTo(10);
            verify(athleticProfileRepository).save(sampleEntity);
        }

        @Test
        @DisplayName("Debe llamar al mapper antes de persistir")
        void shouldCallMapperBeforeSave() {
            when(athleticProfileMapper.toEntity(any())).thenReturn(sampleEntity);
            when(athleticProfileRepository.save(any())).thenReturn(sampleEntity);
            when(athleticProfileMapper.toDTO(any())).thenReturn(sampleDTO);

            athleticProfileService.createAthleticProfile(sampleDTO);

            // El orden importa: primero toEntity, luego save, luego toDTO
            var order = inOrder(athleticProfileMapper, athleticProfileRepository);
            order.verify(athleticProfileMapper).toEntity(sampleDTO);
            order.verify(athleticProfileRepository).save(sampleEntity);
            order.verify(athleticProfileMapper).toDTO(sampleEntity);
        }
    }

    // ── updateAthleticProfile ────────────────────────────────────────────────

    @Nested
    @DisplayName("updateAthleticProfile()")
    class Update {

        @Test
        @DisplayName("Debe actualizar campos y retornar DTO")
        void shouldUpdateAndReturnDTO() {
            AthleticProfileDTO updatedDTO = new AthleticProfileDTO();
            updatedDTO.setDorsalNumber(7);
            updatedDTO.setPosition("volante");
            updatedDTO.setLaterality("zurdo");
            updatedDTO.setStature("180cm");
            updatedDTO.setState("activo");

            when(athleticProfileRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(athleticProfileRepository.save(sampleEntity)).thenReturn(sampleEntity);
            when(athleticProfileMapper.toDTO(sampleEntity)).thenReturn(updatedDTO);

            AthleticProfileDTO result = athleticProfileService.updateAthleticProfile(1L, updatedDTO);

            assertThat(result.getDorsalNumber()).isEqualTo(7);
            assertThat(result.getPosition()).isEqualTo("volante");
            verify(athleticProfileRepository).save(sampleEntity);
        }

        @Test
        @DisplayName("Debe lanzar NoSuchElementException cuando el perfil no existe")
        void shouldThrowWhenNotFound() {
            when(athleticProfileRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> athleticProfileService.updateAthleticProfile(99L, sampleDTO))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("99");
        }

        @Test
        @DisplayName("Debe actualizar todos los campos mutables del perfil")
        void shouldUpdateAllMutableFields() {
            AthleticProfileDTO updatedDTO = new AthleticProfileDTO();
            updatedDTO.setDorsalNumber(5);
            updatedDTO.setPosition("defensa");
            updatedDTO.setLaterality("diestro");
            updatedDTO.setStature("170cm");
            updatedDTO.setState("inactivo");

            when(athleticProfileRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(athleticProfileRepository.save(any())).thenReturn(sampleEntity);
            when(athleticProfileMapper.toDTO(sampleEntity)).thenReturn(updatedDTO);

            AthleticProfileDTO result = athleticProfileService.updateAthleticProfile(1L, updatedDTO);

            // Verificar que la entidad recibió los nuevos valores
            assertThat(sampleEntity.getDorsalNumber()).isEqualTo(5);
            assertThat(sampleEntity.getPosition()).isEqualTo("defensa");
            assertThat(sampleEntity.getLaterality()).isEqualTo("diestro");
            assertThat(sampleEntity.getStature()).isEqualTo("170cm");
            assertThat(sampleEntity.getState()).isEqualTo("inactivo");
        }
    }

    // ── deleteAthleticProfile ────────────────────────────────────────────────

    @Nested
    @DisplayName("deleteAthleticProfile()")
    class Delete {

        @Test
        @DisplayName("Debe eliminar el perfil cuando existe")
        void shouldDeleteWhenExists() {
            when(athleticProfileRepository.existsById(1L)).thenReturn(true);

            athleticProfileService.deleteAthleticProfile(1L);

            verify(athleticProfileRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException cuando el perfil no existe")
        void shouldThrowWhenNotFound() {
            when(athleticProfileRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> athleticProfileService.deleteAthleticProfile(99L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Athletic profile not found");

            verify(athleticProfileRepository, never()).deleteById(any());
        }
    }
}
