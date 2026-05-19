package edu.eci.userService.services;

import org.springframework.stereotype.Service;
import java.util.*;
import edu.eci.userService.repository.AthleticProfileRepository;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.mappers.AthleticProfileMapper;
import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.validation.ValidationUtils;
import edu.eci.userService.exception.UserNotFoundException;

@Service
public class AthleticProfileService {

    private final AthleticProfileRepository athleticProfileRepository;
    private final UserRepository userRepository;
    private final AthleticProfileMapper athleticProfileMapper;

    public AthleticProfileService(AthleticProfileRepository athleticProfileRepository,
            UserRepository userRepository,
            AthleticProfileMapper athleticProfileMapper) {
        this.athleticProfileRepository = athleticProfileRepository;
        this.userRepository = userRepository;
        this.athleticProfileMapper = athleticProfileMapper;
    }

    public List<AthleticProfileDTO> getAllAthleticProfiles() {
        List<AthleticProfileDTO> athleticProfileDTOs = new ArrayList<>();
        for (AthleticProfileEntity entity : athleticProfileRepository.findAll()) {
            athleticProfileDTOs.add(athleticProfileMapper.toDTO(entity));
        }
        return athleticProfileDTOs;
    }

    public AthleticProfileDTO getAthleticProfileByUserId(Long userId) {
        return athleticProfileRepository.findByUserId(userId)
                .map(athleticProfileMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("Athletic profile not found for user: " + userId));
    }

    public List<AthleticProfileDTO> getAthleticProfileByPosition(String position) {
        List<AthleticProfileDTO> dtos = new ArrayList<>();
        for (AthleticProfileEntity entity : athleticProfileRepository.findByPosition(position)) {
            dtos.add(athleticProfileMapper.toDTO(entity));
        }
        return dtos;
    }

    public List<AthleticProfileDTO> getAthleticProfileByLaterality(String laterality) {
        List<AthleticProfileDTO> dtos = new ArrayList<>();
        for (AthleticProfileEntity entity : athleticProfileRepository.findByLaterality(laterality)) {
            dtos.add(athleticProfileMapper.toDTO(entity));
        }
        return dtos;
    }

    public AthleticProfileDTO createAthleticProfile(Long userId, AthleticProfileDTO athleticProfileDTO) {
        // Validaciones
        if (!ValidationUtils.isValidDorsal(athleticProfileDTO.getDorsalNumber())) {
            throw new IllegalArgumentException(ValidationUtils.getValidationErrorMessage("dorsal", ""));
        }
        if (!ValidationUtils.isValidPosition(athleticProfileDTO.getPosition())) {
            throw new IllegalArgumentException(ValidationUtils.getValidationErrorMessage("position", ""));
        }
        if (!ValidationUtils.isValidStature(athleticProfileDTO.getStature())) {
            throw new IllegalArgumentException(ValidationUtils.getValidationErrorMessage("stature", ""));
        }

        // Verificar que el usuario existe
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        // Verificar que no exista un profile previo
        if (athleticProfileRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("User already has an athletic profile");
        }

        AthleticProfileEntity entity = athleticProfileMapper.toEntity(athleticProfileDTO);
        entity.setUser(user);
        return athleticProfileMapper.toDTO(athleticProfileRepository.save(entity));
    }

    public AthleticProfileDTO updateAthleticProfile(Long userId, AthleticProfileDTO athleticProfileDTO) {
        AthleticProfileEntity entity = athleticProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Athletic profile not found for user: " + userId));

        // Validaciones
        if (athleticProfileDTO.getDorsalNumber() != null &&
                !ValidationUtils.isValidDorsal(athleticProfileDTO.getDorsalNumber())) {
            throw new IllegalArgumentException(ValidationUtils.getValidationErrorMessage("dorsal", ""));
        }
        if (athleticProfileDTO.getPosition() != null &&
                !ValidationUtils.isValidPosition(athleticProfileDTO.getPosition())) {
            throw new IllegalArgumentException(ValidationUtils.getValidationErrorMessage("position", ""));
        }
        if (athleticProfileDTO.getStature() != null &&
                !ValidationUtils.isValidStature(athleticProfileDTO.getStature())) {
            throw new IllegalArgumentException(ValidationUtils.getValidationErrorMessage("stature", ""));
        }

        // Actualizar solo los campos permitidos
        if (athleticProfileDTO.getDorsalNumber() != null) {
            entity.setDorsalNumber(athleticProfileDTO.getDorsalNumber());
        }
        if (athleticProfileDTO.getPosition() != null) {
            entity.setPosition(athleticProfileDTO.getPosition());
        }
        if (athleticProfileDTO.getLaterality() != null) {
            entity.setLaterality(athleticProfileDTO.getLaterality());
        }
        if (athleticProfileDTO.getStature() != null) {
            entity.setStature(athleticProfileDTO.getStature());
        }
        if (athleticProfileDTO.getPhotoUrl() != null) {
            entity.setPhotoUrl(athleticProfileDTO.getPhotoUrl());
        }
        if (athleticProfileDTO.getStatus() != null) {
            entity.setStatus(athleticProfileDTO.getStatus());
        }

        return athleticProfileMapper.toDTO(athleticProfileRepository.save(entity));
    }

    public void deleteAthleticProfile(Long userId) {
        if (!athleticProfileRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("Athletic profile not found for user: " + userId);
        }
        // Bloquear la eliminación de perfiles deportivos para preservar integridad
        // histórica
        throw new IllegalArgumentException(
                "Athletic profile deletion is not permitted to preserve tournament historical integrity");
    }

}
