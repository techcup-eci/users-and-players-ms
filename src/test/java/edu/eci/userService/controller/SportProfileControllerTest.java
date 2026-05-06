package com.techcup.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcup.users.dto.CreateSportProfileRequest;
import com.techcup.users.dto.UpdateSportProfileRequest;
import com.techcup.users.exception.DeleteSportProfileNotAllowedException;
import com.techcup.users.exception.PlayerAlreadyAssignedToTeamException;
import com.techcup.users.exception.SportProfileAlreadyExistsException;
import com.techcup.users.exception.UserNotFoundException;
import com.techcup.users.model.SportProfile;
import com.techcup.users.model.enums.PlayingPosition;
import com.techcup.users.service.SportProfileService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * REST layer tests for SportProfileController.
 *
 * Architecture note:
 * This microservice runs behind the Orchestrator (API Gateway).
 * The gateway handles token validation and blocks unauthenticated requests.
 * @AutoConfigureMockMvc(addFilters = false) disables Spring Security filters
 * locally since token validation is not the responsibility of this microservice.
 *
 * Endpoints:
 *   GET    /api/users/{id}/sport-profile
 *   POST   /api/users/{id}/sport-profile
 *   PUT    /api/users/{id}/sport-profile
 *   DELETE /api/users/{id}/sport-profile  (always 405 - not allowed by business rule)
 */
@WebMvcTest(SportProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("SportProfileController - REST Tests")
class SportProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportProfileService sportProfileService;

    @Autowired
    private ObjectMapper objectMapper;

    private SportProfile baseProfile;
    private CreateSportProfileRequest createRequest;
    private UpdateSportProfileRequest updateRequest;

    @BeforeEach
    void setUp() {
        baseProfile = new SportProfile();
        baseProfile.setId(10L);
        baseProfile.setPosition(PlayingPosition.FORWARD);
        baseProfile.setJerseyNumber(9);
        baseProfile.setPhotoUrl("https://storage/photo.jpg");
        baseProfile.setAssignedToTeam(false);

        createRequest = new CreateSportProfileRequest();
        createRequest.setPosition(PlayingPosition.FORWARD);
        createRequest.setJerseyNumber(9);
        createRequest.setPhotoUrl("https://storage/photo.jpg");

        updateRequest = new UpdateSportProfileRequest();
        updateRequest.setPosition(PlayingPosition.GOALKEEPER);
        updateRequest.setJerseyNumber(1);
        updateRequest.setPhotoUrl("https://storage/new-photo.jpg");
    }

    // ----------------------------------------------------------------
    // GET /api/users/{id}/sport-profile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("GET /api/users/{id}/sport-profile - Find sport profile")
    class FindSportProfileTests {

        @Test
        @DisplayName("Must return 200 with sport profile data when it exists")
        void mustReturn200WithSportProfileDataWhenExists() throws Exception {
            when(sportProfileService.getProfileByUser(1L)).thenReturn(baseProfile);

            mockMvc.perform(get("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value("FORWARD"))
                .andExpect(jsonPath("$.jerseyNumber").value(9));
        }

        @Test
        @DisplayName("Must return 404 when player has no sport profile")
        void mustReturn404WhenPlayerHasNoSportProfile() throws Exception {
            when(sportProfileService.getProfileByUser(1L))
                .thenThrow(new UserNotFoundException("Sport profile not found"));

            mockMvc.perform(get("/api/users/1/sport-profile"))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Must return 400 when user ID in URL is not a valid number")
        void mustReturn400WhenUserIdIsNotAValidNumber() throws Exception {
            mockMvc.perform(get("/api/users/abc/sport-profile"))
                .andExpect(status().isBadRequest());
        }
    }

    // ----------------------------------------------------------------
    // POST /api/users/{id}/sport-profile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("POST /api/users/{id}/sport-profile - Create sport profile")
    class CreateSportProfileTests {

        @Test
        @DisplayName("Must return 201 when sport profile is created with valid data")
        void mustReturn201WhenSportProfileIsCreatedWithValidData() throws Exception {
            when(sportProfileService.createProfile(eq(1L), any(), anyInt(), any()))
                .thenReturn(baseProfile);

            mockMvc.perform(post("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.position").value("FORWARD"))
                .andExpect(jsonPath("$.jerseyNumber").value(9));
        }

        @Test
        @DisplayName("Must return 409 when player already has a sport profile")
        void mustReturn409WhenPlayerAlreadyHasSportProfile() throws Exception {
            when(sportProfileService.createProfile(eq(1L), any(), anyInt(), any()))
                .thenThrow(new SportProfileAlreadyExistsException("Player already has a profile"));

            mockMvc.perform(post("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Must return 400 when playing position is null")
        void mustReturn400WhenPlayingPositionIsNull() throws Exception {
            createRequest.setPosition(null);

            mockMvc.perform(post("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Must return 400 when jersey number is zero")
        void mustReturn400WhenJerseyNumberIsZero() throws Exception {
            createRequest.setJerseyNumber(0);

            mockMvc.perform(post("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Must return 400 when jersey number is negative")
        void mustReturn400WhenJerseyNumberIsNegative() throws Exception {
            createRequest.setJerseyNumber(-1);

            mockMvc.perform(post("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Must return 400 when jersey number is greater than 99")
        void mustReturn400WhenJerseyNumberIsGreaterThan99() throws Exception {
            createRequest.setJerseyNumber(100);

            mockMvc.perform(post("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Must return 201 when photo URL is null since photo is optional")
        void mustReturn201WhenPhotoUrlIsNullSincePhotoIsOptional() throws Exception {
            createRequest.setPhotoUrl(null);
            when(sportProfileService.createProfile(eq(1L), any(), anyInt(), eq(null)))
                .thenReturn(baseProfile);

            mockMvc.perform(post("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Must return 404 when user does not exist on create")
        void mustReturn404WhenUserDoesNotExistOnCreate() throws Exception {
            when(sportProfileService.createProfile(eq(99L), any(), anyInt(), any()))
                .thenThrow(new UserNotFoundException("User not found"));

            mockMvc.perform(post("/api/users/99/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Must return 400 when request body is empty")
        void mustReturn400WhenRequestBodyIsEmpty() throws Exception {
            mockMvc.perform(post("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().isBadRequest());
        }
    }

    // ----------------------------------------------------------------
    // PUT /api/users/{id}/sport-profile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("PUT /api/users/{id}/sport-profile - Update sport profile")
    class UpdateSportProfileTests {

        @Test
        @DisplayName("Must return 200 when profile is updated and player is not in a team")
        void mustReturn200WhenProfileIsUpdatedAndPlayerIsNotInTeam() throws Exception {
            baseProfile.setPosition(PlayingPosition.GOALKEEPER);
            baseProfile.setJerseyNumber(1);
            when(sportProfileService.updateProfile(eq(1L), any(), any(), any()))
                .thenReturn(baseProfile);

            mockMvc.perform(put("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value("GOALKEEPER"))
                .andExpect(jsonPath("$.jerseyNumber").value(1));
        }

        @Test
        @DisplayName("Must return 409 when player is assigned to a team")
        void mustReturn409WhenPlayerIsAssignedToTeam() throws Exception {
            when(sportProfileService.updateProfile(eq(1L), any(), any(), any()))
                .thenThrow(new PlayerAlreadyAssignedToTeamException("Player is in an active team"));

            mockMvc.perform(put("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Must return 404 when sport profile does not exist on update")
        void mustReturn404WhenSportProfileDoesNotExistOnUpdate() throws Exception {
            when(sportProfileService.updateProfile(eq(1L), any(), any(), any()))
                .thenThrow(new UserNotFoundException("Sport profile not found"));

            mockMvc.perform(put("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Must return 400 when request body is empty")
        void mustReturn400WhenRequestBodyIsEmpty() throws Exception {
            mockMvc.perform(put("/api/users/1/sport-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().isBadRequest());
        }
    }

    // ----------------------------------------------------------------
    // DELETE /api/users/{id}/sport-profile (always 405)
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("DELETE /api/users/{id}/sport-profile - Delete sport profile (not allowed)")
    class DeleteSportProfileTests {

        @Test
        @DisplayName("Must return 405 when attempting to delete a sport profile")
        void mustReturn405WhenAttemptingToDeleteSportProfile() throws Exception {
            doThrow(new DeleteSportProfileNotAllowedException("Deleting a sport profile is not allowed"))
                .when(sportProfileService).deleteProfile(any());

            mockMvc.perform(delete("/api/users/1/sport-profile"))
                .andExpect(status().isMethodNotAllowed());
        }

        @Test
        @DisplayName("Must return 405 regardless of whether profile exists or not")
        void mustReturn405RegardlessOfWhetherProfileExists() throws Exception {
            mockMvc.perform(delete("/api/users/99/sport-profile"))
                .andExpect(status().isMethodNotAllowed());
        }
    }
}
