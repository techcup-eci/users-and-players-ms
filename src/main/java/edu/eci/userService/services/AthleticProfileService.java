package edu.eci.userService.services;

import org.springframework.stereotype.Service;

import edu.eci.userService.repository.AthleticProfileRepository;
import edu.eci.userService.mappers.AthleticProfileMapper;

@Service
public class AthleticProfileService {

    private final AthleticProfileRepository athleticProfileRepository;
    private final AthleticProfileMapper athleticProfileMapper;

}
