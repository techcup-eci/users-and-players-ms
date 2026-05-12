package edu.eci.userService.services;

import org.springframework.stereotype.Service;
import java.util.*;
import edu.eci.userService.repository.AthleticProfileRepository;
import edu.eci.userService.mappers.AthleticProfileMapper;
import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.AthleticProfileEntity;

@Service
public class AthleticProfileService {

    private final AthleticProfileRepository athleticProfileRepository;
    private final AthleticProfileMapper athleticProfileMapper;

    public AthleticProfileService(AthleticProfileRepository athleticProfileRepository,
            AthleticProfileMapper athleticProfileMapper) {
        this.athleticProfileRepository = athleticProfileRepository;
        this.athleticProfileMapper = athleticProfileMapper;
    }

    public List<AthleticProfileDTO> getAllAthleticProfiles() {
        List<AthleticProfileDTO> athleticProfileDTO = new ArrayList<>();
        for (AthleticProfileEntity entity : athleticProfileRepository.findAll()) {
            athleticProfileDTO.add(athleticProfileMapper.toDTO(entity));
        }
        return athleticProfileDTO;
    }

    public AthleticProfileDTO getAthleticProfilesByEmail(String email) {
        AthleticProfileDTO dto = new AthleticProfileDTO();
        dto = athleticProfileMapper.toDTO(athleticProfileRepository.findByEmail(email));
        return dto;
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
        AthleticProfileEntity entity = athleticProfileMapper.toEntity(athleticProfileDTO);
        return athleticProfileMapper.toDTO(athleticProfileRepository.save(entity));
    }

    public AthleticProfileDTO updateAthleticProfile(String email, AthleticProfileDTO athleticProfileDTO) {
        AthleticProfileEntity entitie = athleticProfileRepository.findByEmail(email);
        if (entitie == null) {
            throw new NoSuchElementException("No athletic profile found with email: " + email);
        }
        entitie.setDorsalNumber(athleticProfileDTO.getDorsalNumber());
        entitie.setPosition(athleticProfileDTO.getPosition());
        entitie.setLaterality(athleticProfileDTO.getLaterality());
        entitie.setStature(athleticProfileDTO.getStature());
        entitie.setState(athleticProfileDTO.getState());
        return athleticProfileMapper.toDTO(athleticProfileRepository.save(entitie));
    }

    public void deleteAthleticProfile(String email) {
        if (!athleticProfileRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Athletic profile not found");
        }
        athleticProfileRepository.deleteByEmail(email);
    }

}
