package edu.eci.userService.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.LateralityType;
import edu.eci.userService.enums.ProfileStatus;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.mappers.AthleticProfileMapper;
import edu.eci.userService.repository.AthleticProfileRepository;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.services.AthleticProfileService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AthleticProfileServiceValidationTest {

    @Mock
    private AthleticProfileRepository athleticProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AthleticProfileMapper athleticProfileMapper;

    @InjectMocks
    private AthleticProfileService athleticProfileService;

    private UserEntity testUser;
    private AthleticProfileEntity testProfile;
    private AthleticProfileDTO testProfileDTO;

    @BeforeEach
    public void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);

        testProfile = new AthleticProfileEntity();
        testProfile.setId(1L);
        testProfile.setUser(testUser);
        testProfile.setDorsalNumber(10);
        testProfile.setPosition("Goalkeeper");
        testProfile.setLaterality(LateralityType.RIGHT);
        testProfile.setStature(185);
        testProfile.setStatus(ProfileStatus.ACTIVE);

        testProfileDTO = new AthleticProfileDTO();
        testProfileDTO.setId(1L);
        testProfileDTO.setUserId(1L);
        testProfileDTO.setDorsalNumber(10);
        testProfileDTO.setPosition("Goalkeeper");
        testProfileDTO.setLaterality(LateralityType.RIGHT);
        testProfileDTO.setStature(185);
        testProfileDTO.setStatus(ProfileStatus.ACTIVE);
    }

    @Test
    public void testCreateAthleticProfile_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(athleticProfileRepository.existsByUserId(1L)).thenReturn(false);
        when(athleticProfileRepository.save(any(AthleticProfileEntity.class))).thenReturn(testProfile);
        when(athleticProfileMapper.toDTO(testProfile)).thenReturn(testProfileDTO);

        AthleticProfileDTO result = athleticProfileService.createAthleticProfile(1L, testProfileDTO);

        assertThat(result).isNotNull();
        assertThat(result.getDorsalNumber()).isEqualTo(10);
        verify(athleticProfileRepository).save(any(AthleticProfileEntity.class));
    }

    @Test
    public void testCreateAthleticProfile_InvalidDorsal_OutOfRange() {
        AthleticProfileDTO invalidDTO = new AthleticProfileDTO();
        invalidDTO.setDorsalNumber(150);

        assertThatThrownBy(() -> athleticProfileService.createAthleticProfile(1L, invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("dorsal");
    }

    @Test
    public void testCreateAthleticProfile_InvalidStature_TooTall() {
        testProfileDTO.setStature(350);

        assertThatThrownBy(() -> athleticProfileService.createAthleticProfile(1L, testProfileDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("estatura");
    }

    @Test
    public void testCreateAthleticProfile_InvalidStature_TooShort() {
        testProfileDTO.setStature(50);

        assertThatThrownBy(() -> athleticProfileService.createAthleticProfile(1L, testProfileDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("estatura");
    }

    @Test
    public void testCreateAthleticProfile_UserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> athleticProfileService.createAthleticProfile(99L, testProfileDTO))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testCreateAthleticProfile_UserAlreadyHasProfile() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(athleticProfileRepository.existsByUserId(1L)).thenReturn(true);

        assertThatThrownBy(() -> athleticProfileService.createAthleticProfile(1L, testProfileDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already has an athletic profile");
    }

    @Test
    public void testUpdateAthleticProfile_Success() {
        when(athleticProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(athleticProfileRepository.save(any(AthleticProfileEntity.class))).thenReturn(testProfile);
        when(athleticProfileMapper.toDTO(testProfile)).thenReturn(testProfileDTO);

        AthleticProfileDTO result = athleticProfileService.updateAthleticProfile(1L, testProfileDTO);

        assertThat(result).isNotNull();
        verify(athleticProfileRepository).save(any(AthleticProfileEntity.class));
    }

    @Test
    public void testUpdateAthleticProfile_InvalidDorsal() {
        AthleticProfileDTO invalidDTO = new AthleticProfileDTO();
        invalidDTO.setDorsalNumber(200);

        when(athleticProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));

        assertThatThrownBy(() -> athleticProfileService.updateAthleticProfile(1L, invalidDTO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGetAthleticProfileByUserId_Success() {
        when(athleticProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(athleticProfileMapper.toDTO(testProfile)).thenReturn(testProfileDTO);

        AthleticProfileDTO result = athleticProfileService.getAthleticProfileByUserId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
    }

    @Test
    public void testGetAthleticProfileByUserId_NotFound() {
        when(athleticProfileRepository.findByUserId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> athleticProfileService.getAthleticProfileByUserId(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testDeleteAthleticProfile_NotPermitted() {
        when(athleticProfileRepository.existsByUserId(1L)).thenReturn(true);

        assertThatThrownBy(() -> athleticProfileService.deleteAthleticProfile(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("deletion is not permitted");
    }
}
