package edu.eci.userService.services;

import org.springframework.stereotype.Service;
import java.util.*;
import edu.eci.userService.repository.AthleticProfileRepository;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.mappers.AthleticProfileMapper;
import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;
import edu.eci.userService.entities.UserEntity;

@Service
public class AthleticProfileService {

    private final AthleticProfileRepository athleticProfileRepository;
    private final AthleticProfileMapper athleticProfileMapper;
    private final UserRepository userRepository;

    public AthleticProfileService(AthleticProfileRepository athleticProfileRepository,
            AthleticProfileMapper athleticProfileMapper,
            UserRepository userRepository) {
        this.athleticProfileRepository = athleticProfileRepository;
        this.athleticProfileMapper = athleticProfileMapper;
        this.userRepository = userRepository;
    }

    public List<AthleticProfileDTO> getAllAthleticProfiles() {
        List<AthleticProfileDTO> athleticProfileDTO = new ArrayList<>();
        for (AthleticProfileEntity entity : athleticProfileRepository.findAll()) {
            athleticProfileDTO.add(athleticProfileMapper.toDTO(entity));
        }
        return athleticProfileDTO;
    }

    public AthleticProfileDTO getAthleticProfilesByUserId(Long userId) {
        return athleticProfileRepository.findById(userId)
                .map(athleticProfileMapper::toDTO)
                .orElseThrow(() -> new NoSuchElementException("No athletic profile found for userId: " + userId));
    }

    public AthleticProfileDTO getAthleticProfileByEmail(String email) {
        return athleticProfileRepository.findByUserEmail(email)
                .map(athleticProfileMapper::toDTO)
                .orElseThrow(() -> new NoSuchElementException("No athletic profile found for email: " + email));
    }

    public List<AthleticProfileDTO> getAthleticProfileByPosition(String position) {
        List<AthleticProfileDTO> dto = new ArrayList<>();
        for (AthleticProfileEntity entity : athleticProfileRepository.findByPosition(position)) {
            dto.add(athleticProfileMapper.toDTO(entity));
        }
        return dto;
    }

    public List<AthleticProfileDTO> getAthleticProfileByLaterality(String laterality) {
        List<AthleticProfileDTO> dto = new ArrayList<>();
        for (AthleticProfileEntity entity : athleticProfileRepository.findByLaterality(laterality)) {
            dto.add(athleticProfileMapper.toDTO(entity));
        }
        return dto;
    }

    public AthleticProfileDTO createAthleticProfile(AthleticProfileDTO athleticProfileDTO) {
        UserEntity user = userRepository.findByEmail(athleticProfileDTO.getEmail());
        if (user == null) {
            throw new NoSuchElementException("User not found with email: " + athleticProfileDTO.getEmail());
        }

        // If profile already exists, update it instead of creating a duplicate
        if (user.getAthleticProfile() != null) {
            AthleticProfileEntity existing = user.getAthleticProfile();
            existing.setDorsalNumber(athleticProfileDTO.getDorsalNumber());
            existing.setPosition(athleticProfileDTO.getPosition());
            existing.setLaterality(athleticProfileDTO.getLaterality());
            existing.setStature(athleticProfileDTO.getStature());
            existing.setState(athleticProfileDTO.getState());
            if (existing.getNickName() == null || existing.getNickName().isBlank()) {
                existing.setNickName(
                        user.getName() != null ? user.getName().split("@")[0] : user.getEmail().split("@")[0]);
            }
            return athleticProfileMapper.toDTO(athleticProfileRepository.save(existing));
        }

        // Create new profile — build manually to avoid mapper setting ID (conflicts
        // with @MapsId)
        AthleticProfileEntity entity = new AthleticProfileEntity();
        entity.setDorsalNumber(athleticProfileDTO.getDorsalNumber());
        entity.setPosition(athleticProfileDTO.getPosition());
        entity.setLaterality(athleticProfileDTO.getLaterality());
        entity.setStature(athleticProfileDTO.getStature());
        entity.setState(athleticProfileDTO.getState());

        // Generate nickName if not provided
        String nickName = athleticProfileDTO.getNickName();
        if (nickName == null || nickName.isBlank()) {
            String base = user.getName() != null ? user.getName() : user.getEmail();
            nickName = base.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
        }
        entity.setNickName(nickName);

        // Maintain BOTH sides of the bidirectional @OneToOne relationship
        entity.setUser(user);
        user.setAthleticProfile(entity);

        return athleticProfileMapper.toDTO(athleticProfileRepository.save(entity));
    }

    public AthleticProfileDTO updateAthleticProfile(Long userId, AthleticProfileDTO athleticProfileDTO) {
        AthleticProfileEntity entity = athleticProfileRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("No athletic profile found with user ID: " + userId));

        // Update only the fields provided — preserve existing nickName
        if (athleticProfileDTO.getDorsalNumber() != 0) {
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
        if (athleticProfileDTO.getState() != null) {
            entity.setState(athleticProfileDTO.getState());
        }
        return athleticProfileMapper.toDTO(athleticProfileRepository.save(entity));
    }

    public void deleteAthleticProfile(Long userId) {
        if (!athleticProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("Athletic profile not found");
        }
        athleticProfileRepository.deleteById(userId);
    }

}
