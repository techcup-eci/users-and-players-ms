package edu.eci.userService.controller;

import edu.eci.userService.services.AthleticProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/AthleticProfile")
public class AthleticProfileController {

    private final AthleticProfileService athleticProfileService;

    public AthleticProfileController(AthleticProfileService athleticProfileService) {
        this.athleticProfileService = athleticProfileService;
    }

    @GetMapping("/ping")
    public String ping() {
        return athleticProfileService.hello();
    }

}
