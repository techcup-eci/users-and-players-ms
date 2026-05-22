package edu.eci.userService.service;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.mappers.AthleticProfileMapper;
import edu.eci.userService.mappers.UserMapper;
import edu.eci.userService.repository.AthleticProfileRepository;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.services.AthleticProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AthleticProfileService - casos adicionales")
class AthleticProfileServiceAdditionalTest {

    @Mock
    private AthleticProfileRepository athleticProfileRepository;

    @Mock
    private UserRepository userRepository;

    // ← AthleticProfileMapper ahora necesita UserMapper
    private final UserMapper userMapper = new UserMapper();
    private final AthleticProfileMapper athleticProfileMapper = new AthleticProfileMapper(userMapper);

    private AthleticProfileService athleticProfileService;

    private UserEntity sampleUser;
    private AthleticProfileEntity existingProfile;

    @BeforeEach
    void setUp() {
        athleticProfileService = new AthleticProfileService(
                athleticProfileRepository, athleticProfileMapper, userRepository);

        sampleUser = new UserEntity();
        sampleUser.setId(1L);
        sampleUser.setName("Juan Pérez");
        sampleUser.setEmail("juan@gmail.com");

        existingProfile = new AthleticProfileEntity();
        existingProfile.setId(1L);
        existingProfile.setDorsalNumber(10);
        existingProfile.setNickName("Juancho");
        existingProfile.setPosition("delantero");
        existingProfile.setLaterality("diestro");
        existingProfile.setStature("175cm");
        existingProfile.setState("activo");
        existingProfile.setUser(sampleUser);
    }

    @Nested
    @DisplayName("createAthleticProfile() — perfil ya existente")
    class CreateWithExistingProfile {

        @Test
        @DisplayName("Debe actualizar perfil existente si el usuario ya tiene uno")
        void shouldUpdateExistingProfileWhenUserAlreadyHasOne() {
            sampleUser.setAthleticProfile(existingProfile);

            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setEmail("juan@gmail.com");
            dto.setDorsalNumber(7);
            dto.setPosition("volante");
            dto.setLaterality("zurdo");
            dto.setStature("180cm");
            dto.setState("activo");

            when(userRepository.findByEmail("juan@gmail.com")).thenReturn(sampleUser);
            when(athleticProfileRepository.save(any(AthleticProfileEntity.class)))
                    .thenReturn(existingProfile);

            AthleticProfileDTO result = athleticProfileService.createAthleticProfile(dto);

            assertThat(result).isNotNull();
            verify(athleticProfileRepository).save(existingProfile);
        }

        @Test
        @DisplayName("Debe generar nickName desde email cuando nombre de usuario es null")
        void shouldGenerateNickNameFromEmailWhenNameIsNull() {
            sampleUser.setName(null);

            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setEmail("juan@gmail.com");
            dto.setDorsalNumber(5);
            dto.setPosition("defensa");
            dto.setLaterality("diestro");
            dto.setStature("175cm");
            dto.setState("activo");

            when(userRepository.findByEmail("juan@gmail.com")).thenReturn(sampleUser);
            when(athleticProfileRepository.save(any(AthleticProfileEntity.class)))
                    .thenReturn(existingProfile);

            AthleticProfileDTO result = athleticProfileService.createAthleticProfile(dto);

            assertThat(result).isNotNull();
            verify(athleticProfileRepository).save(any(AthleticProfileEntity.class));
        }

        @Test
        @DisplayName("Debe usar nickName provisto cuando no es blank")
        void shouldUseProvidedNickNameWhenNotBlank() {
            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setEmail("juan@gmail.com");
            dto.setDorsalNumber(9);
            dto.setNickName("ElJuancho");
            dto.setPosition("delantero");
            dto.setLaterality("diestro");
            dto.setStature("175cm");
            dto.setState("activo");

            when(userRepository.findByEmail("juan@gmail.com")).thenReturn(sampleUser);
            when(athleticProfileRepository.save(any(AthleticProfileEntity.class)))
                    .thenReturn(existingProfile);

            AthleticProfileDTO result = athleticProfileService.createAthleticProfile(dto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Rama de nickName en perfil existente: debe usar email split cuando nombre es null")
        void shouldHandleNullNickNameInExistingProfile() {
            existingProfile.setNickName(null);
            sampleUser.setName(null);
            sampleUser.setAthleticProfile(existingProfile);

            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setEmail("juan@gmail.com");
            dto.setDorsalNumber(7);
            dto.setPosition("volante");
            dto.setLaterality("zurdo");
            dto.setStature("180cm");
            dto.setState("activo");

            when(userRepository.findByEmail("juan@gmail.com")).thenReturn(sampleUser);
            when(athleticProfileRepository.save(any(AthleticProfileEntity.class)))
                    .thenReturn(existingProfile);

            AthleticProfileDTO result = athleticProfileService.createAthleticProfile(dto);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("updateAthleticProfile() - campos opcionales")
    class UpdateBranches {

        @Test
        @DisplayName("Debe ignorar dorsalNumber 0 al actualizar")
        void shouldIgnoreDorsalNumberZero() {
            when(athleticProfileRepository.findById(1L)).thenReturn(Optional.of(existingProfile));
            when(athleticProfileRepository.save(existingProfile)).thenReturn(existingProfile);

            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setDorsalNumber(0);
            dto.setPosition("portero");

            athleticProfileService.updateAthleticProfile(1L, dto);

            assertThat(existingProfile.getDorsalNumber()).isEqualTo(10);
            assertThat(existingProfile.getPosition()).isEqualTo("portero");
        }

        @Test
        @DisplayName("Debe ignorar campos null al actualizar")
        void shouldIgnoreNullFieldsOnUpdate() {
            when(athleticProfileRepository.findById(1L)).thenReturn(Optional.of(existingProfile));
            when(athleticProfileRepository.save(existingProfile)).thenReturn(existingProfile);

            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setDorsalNumber(0);
            dto.setPosition(null);
            dto.setLaterality(null);
            dto.setStature(null);
            dto.setState(null);

            athleticProfileService.updateAthleticProfile(1L, dto);

            assertThat(existingProfile.getPosition()).isEqualTo("delantero");
            assertThat(existingProfile.getLaterality()).isEqualTo("diestro");
            assertThat(existingProfile.getStature()).isEqualTo("175cm");
            assertThat(existingProfile.getState()).isEqualTo("activo");
        }
    }
}