package edu.eci.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.services.AthleticProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * REST layer tests for AthleticProfileController.
 *
 * Architecture note:
 * This microservice runs behind the Orchestrator (API Gateway) which handles
 * token validation. @AutoConfigureMockMvc(addFilters = false) disables Spring
 * Security filters locally since that responsibility belongs to the gateway.
 *
 * Endpoints covered:
 *   GET    /AthleticProfile
 *   GET    /AthleticProfile/{email}
 *   POST   /AthleticProfile
 *   PUT    /AthleticProfile/{email}
 *   DELETE /AthleticProfile/{email}
 */
@WebMvcTest(AthleticProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AthleticProfileController - REST Tests")
class AthleticProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AthleticProfileService athleticProfileService;

    @Autowired
    private ObjectMapper objectMapper;

    private AthleticProfileDTO baseDTO;
    private AthleticProfileController.AthleticProfileRequestBody validRequestBody;

    @BeforeEach
    void setUp() {
        baseDTO = new AthleticProfileDTO(10, "player@escuela.edu.co", "FORWARD", "RIGHT", "180cm", "ACTIVE");

        validRequestBody = new AthleticProfileController.AthleticProfileRequestBody(
            10,
            "player@escuela.edu.co",
            "FORWARD",
            "RIGHT",
            "180cm",
            "ACTIVE"
        );
    }

    // ----------------------------------------------------------------
    // GET /AthleticProfile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("GET /AthleticProfile - Get all profiles")
    class GetAllAthleticProfilesTests {

        @Test
        @DisplayName("Must return 200 with list of all profiles when profiles exist")
        void mustReturn200WithListOfAllProfilesWhenProfilesExist() throws Exception {
            when(athleticProfileService.getAllAthleticProfiles()).thenReturn(List.of(baseDTO));

            mockMvc.perform(get("/AthleticProfile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("player@escuela.edu.co"))
                .andExpect(jsonPath("$[0].dorsalNumber").value(10))
                .andExpect(jsonPath("$[0].position").value("FORWARD"));
        }

        @Test
        @DisplayName("Must return 200 with empty array when no profiles exist")
        void mustReturn200WithEmptyArrayWhenNoProfilesExist() throws Exception {
            when(athleticProfileService.getAllAthleticProfiles()).thenReturn(List.of());

            mockMvc.perform(get("/AthleticProfile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Must return 200 with multiple profiles when more than one profile exists")
        void mustReturn200WithMultipleProfilesWhenMoreThanOneExists() throws Exception {
            AthleticProfileDTO second = new AthleticProfileDTO(7, "second@escuela.edu.co", "DEFENDER", "LEFT", "175cm", "ACTIVE");
            when(athleticProfileService.getAllAthleticProfiles()).thenReturn(List.of(baseDTO, second));

            mockMvc.perform(get("/AthleticProfile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        }
    }

    // ----------------------------------------------------------------
    // GET /AthleticProfile/{email}
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("GET /AthleticProfile/{email} - Get profile by email")
    class GetProfileByEmailTests {

        @Test
        @DisplayName("Must return 200 with profile data when profile exists for email")
        void mustReturn200WithProfileDataWhenProfileExistsForEmail() throws Exception {
            when(athleticProfileService.getAthleticProfilesByEmail("player@escuela.edu.co"))
                .thenReturn(baseDTO);

            mockMvc.perform(get("/AthleticProfile/player@escuela.edu.co"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("player@escuela.edu.co"))
                .andExpect(jsonPath("$.dorsalNumber").value(10))
                .andExpect(jsonPath("$.position").value("FORWARD"))
                .andExpect(jsonPath("$.laterality").value("RIGHT"))
                .andExpect(jsonPath("$.stature").value("180cm"))
                .andExpect(jsonPath("$.state").value("ACTIVE"));
        }

        @Test
        @DisplayName("Must call service getAthleticProfilesByEmail with the email from the path")
        void mustCallServiceWithEmailFromPath() throws Exception {
            when(athleticProfileService.getAthleticProfilesByEmail("player@escuela.edu.co"))
                .thenReturn(baseDTO);

            mockMvc.perform(get("/AthleticProfile/player@escuela.edu.co"));

            verify(athleticProfileService).getAthleticProfilesByEmail("player@escuela.edu.co");
        }
    }

    // ----------------------------------------------------------------
    // POST /AthleticProfile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("POST /AthleticProfile - Create profile")
    class CreateAthleticProfileTests {

        @Test
        @DisplayName("Must return 200 with created profile data when request is valid")
        void mustReturn200WithCreatedProfileWhenRequestIsValid() throws Exception {
            when(athleticProfileService.createAthleticProfile(any(AthleticProfileDTO.class)))
                .thenReturn(baseDTO);

            mockMvc.perform(post("/AthleticProfile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("player@escuela.edu.co"))
                .andExpect(jsonPath("$.dorsalNumber").value(10))
                .andExpect(jsonPath("$.position").value("FORWARD"));
        }

        @Test
        @DisplayName("Must map request body fields to DTO correctly before calling service")
        void mustMapRequestBodyFieldsToDTOCorrectlyBeforeCallingService() throws Exception {
            when(athleticProfileService.createAthleticProfile(any(AthleticProfileDTO.class)))
                .thenReturn(baseDTO);

            mockMvc.perform(post("/AthleticProfile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequestBody)))
                .andExpect(status().isOk());

            verify(athleticProfileService).createAthleticProfile(any(AthleticProfileDTO.class));
        }

        @Test
        @DisplayName("Must return 200 with laterality and stature in the response")
        void mustReturn200WithAllFieldsInResponse() throws Exception {
            when(athleticProfileService.createAthleticProfile(any(AthleticProfileDTO.class)))
                .thenReturn(baseDTO);

            mockMvc.perform(post("/AthleticProfile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequestBody)))
                .andExpect(jsonPath("$.laterality").value("RIGHT"))
                .andExpect(jsonPath("$.stature").value("180cm"))
                .andExpect(jsonPath("$.state").value("ACTIVE"));
        }
    }

    // ----------------------------------------------------------------
    // PUT /AthleticProfile/{email}
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("PUT /AthleticProfile/{email} - Update profile")
    class UpdateAthleticProfileTests {

        @Test
        @DisplayName("Must return 200 with updated profile when update is successful")
        void mustReturn200WithUpdatedProfileWhenUpdateIsSuccessful() throws Exception {
            AthleticProfileDTO updatedDTO = new AthleticProfileDTO(11, "player@escuela.edu.co", "MIDFIELDER", "LEFT", "175cm", "INACTIVE");

            when(athleticProfileService.updateAthleticProfile(eq("player@escuela.edu.co"), any(AthleticProfileDTO.class)))
                .thenReturn(updatedDTO);

            mockMvc.perform(put("/AthleticProfile/player@escuela.edu.co")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dorsalNumber").value(11))
                .andExpect(jsonPath("$.position").value("MIDFIELDER"));
        }

        @Test
        @DisplayName("Must call service updateAthleticProfile with email from path and DTO from body")
        void mustCallServiceWithEmailFromPathAndDTOFromBody() throws Exception {
            when(athleticProfileService.updateAthleticProfile(eq("player@escuela.edu.co"), any(AthleticProfileDTO.class)))
                .thenReturn(baseDTO);

            mockMvc.perform(put("/AthleticProfile/player@escuela.edu.co")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequestBody)));

            verify(athleticProfileService).updateAthleticProfile(eq("player@escuela.edu.co"), any(AthleticProfileDTO.class));
        }

        @Test
        @DisplayName("Must propagate NoSuchElementException from service when profile not found")
        void mustPropagateNoSuchElementExceptionWhenProfileNotFound() throws Exception {
            when(athleticProfileService.updateAthleticProfile(eq("unknown@escuela.edu.co"), any(AthleticProfileDTO.class)))
                .thenThrow(new NoSuchElementException("No athletic profile found with email: unknown@escuela.edu.co"));

            mockMvc.perform(put("/AthleticProfile/unknown@escuela.edu.co")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequestBody)))
                .andExpect(status().is5xxServerError());
        }
    }

    // ----------------------------------------------------------------
    // DELETE /AthleticProfile/{email}
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("DELETE /AthleticProfile/{email} - Delete profile")
    class DeleteAthleticProfileTests {

        @Test
        @DisplayName("Must return 200 with success message when profile is deleted")
        void mustReturn200WithSuccessMessageWhenProfileIsDeleted() throws Exception {
            doNothing().when(athleticProfileService).deleteAthleticProfile("player@escuela.edu.co");

            mockMvc.perform(delete("/AthleticProfile/player@escuela.edu.co"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
        }

        @Test
        @DisplayName("Must call service deleteAthleticProfile with email from path")
        void mustCallServiceDeleteWithEmailFromPath() throws Exception {
            doNothing().when(athleticProfileService).deleteAthleticProfile("player@escuela.edu.co");

            mockMvc.perform(delete("/AthleticProfile/player@escuela.edu.co"))
                .andExpect(status().isOk());

            verify(athleticProfileService).deleteAthleticProfile("player@escuela.edu.co");
        }

        @Test
        @DisplayName("Must propagate IllegalArgumentException from service when profile not found")
        void mustPropagateIllegalArgumentExceptionWhenProfileNotFound() throws Exception {
            doThrow(new IllegalArgumentException("Athletic profile not found"))
                .when(athleticProfileService).deleteAthleticProfile("unknown@escuela.edu.co");

            mockMvc.perform(delete("/AthleticProfile/unknown@escuela.edu.co"))
                .andExpect(status().is5xxServerError());
        }
    }
}
