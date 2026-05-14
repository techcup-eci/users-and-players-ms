package edu.eci.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.userService.dto.CreateSportProfileRequest;
import edu.eci.userService.dto.UpdateSportProfileRequest;
import edu.eci.userService.exception.DeleteSportProfileNotAllowedException;
import edu.eci.userService.exception.PlayerAlreadyAssignedToTeamException;
import edu.eci.userService.exception.SportProfileAlreadyExistsException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.model.SportProfile;
import edu.eci.userService.model.enums.PlayingPosition;
import edu.eci.userService.services.SportProfileService;
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

@WebMvcTest(controllers = {SportProfileController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("SportProfileController - REST Tests")
class SportProfileControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private SportProfileService sportProfileService;
    @Autowired private ObjectMapper objectMapper;

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

    @Nested @DisplayName("GET /api/users/{id}/sport-profile")
    class FindSportProfileTests {
        @Test void mustReturn200WithSportProfileDataWhenExists() throws Exception {
            when(sportProfileService.getProfileByUser(1L)).thenReturn(baseProfile);
            mockMvc.perform(get("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value("FORWARD"))
                .andExpect(jsonPath("$.jerseyNumber").value(9));
        }
        @Test void mustReturn404WhenPlayerHasNoSportProfile() throws Exception {
            when(sportProfileService.getProfileByUser(1L))
                .thenThrow(new UserNotFoundException("Sport profile not found"));
            mockMvc.perform(get("/api/users/1/sport-profile")).andExpect(status().isNotFound());
        }
        @Test void mustReturn400WhenUserIdIsNotAValidNumber() throws Exception {
            mockMvc.perform(get("/api/users/abc/sport-profile")).andExpect(status().isBadRequest());
        }
    }

    @Nested @DisplayName("POST /api/users/{id}/sport-profile")
    class CreateSportProfileTests {
        @Test void mustReturn201WhenSportProfileIsCreatedWithValidData() throws Exception {
            when(sportProfileService.createProfile(eq(1L), any(), anyInt(), any())).thenReturn(baseProfile);
            mockMvc.perform(post("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.position").value("FORWARD"))
                .andExpect(jsonPath("$.jerseyNumber").value(9));
        }
        @Test void mustReturn409WhenPlayerAlreadyHasSportProfile() throws Exception {
            when(sportProfileService.createProfile(eq(1L), any(), anyInt(), any()))
                .thenThrow(new SportProfileAlreadyExistsException("Player already has a profile"));
            mockMvc.perform(post("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isConflict());
        }
        @Test void mustReturn400WhenPlayingPositionIsNull() throws Exception {
            createRequest.setPosition(null);
            mockMvc.perform(post("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
        }
        @Test void mustReturn400WhenJerseyNumberIsZero() throws Exception {
            createRequest.setJerseyNumber(0);
            mockMvc.perform(post("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
        }
        @Test void mustReturn400WhenJerseyNumberIsNegative() throws Exception {
            createRequest.setJerseyNumber(-1);
            mockMvc.perform(post("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
        }
        @Test void mustReturn400WhenJerseyNumberIsGreaterThan99() throws Exception {
            createRequest.setJerseyNumber(100);
            mockMvc.perform(post("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
        }
        @Test void mustReturn201WhenPhotoUrlIsNullSincePhotoIsOptional() throws Exception {
            createRequest.setPhotoUrl(null);
            when(sportProfileService.createProfile(eq(1L), any(), anyInt(), eq(null))).thenReturn(baseProfile);
            mockMvc.perform(post("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());
        }
        @Test void mustReturn404WhenUserDoesNotExistOnCreate() throws Exception {
            when(sportProfileService.createProfile(eq(99L), any(), anyInt(), any()))
                .thenThrow(new UserNotFoundException("User not found"));
            mockMvc.perform(post("/api/users/99/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isNotFound());
        }
        @Test void mustReturn400WhenRequestBodyIsEmpty() throws Exception {
            mockMvc.perform(post("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content("{}")).andExpect(status().isBadRequest());
        }
    }

    @Nested @DisplayName("PUT /api/users/{id}/sport-profile")
    class UpdateSportProfileTests {
        @Test void mustReturn200WhenProfileIsUpdatedAndPlayerIsNotInTeam() throws Exception {
            baseProfile.setPosition(PlayingPosition.GOALKEEPER);
            baseProfile.setJerseyNumber(1);
            when(sportProfileService.updateProfile(eq(1L), any(), any(), any())).thenReturn(baseProfile);
            mockMvc.perform(put("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value("GOALKEEPER"))
                .andExpect(jsonPath("$.jerseyNumber").value(1));
        }
        @Test void mustReturn409WhenPlayerIsAssignedToTeam() throws Exception {
            when(sportProfileService.updateProfile(eq(1L), any(), any(), any()))
                .thenThrow(new PlayerAlreadyAssignedToTeamException("Player is in an active team"));
            mockMvc.perform(put("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isConflict());
        }
        @Test void mustReturn404WhenSportProfileDoesNotExistOnUpdate() throws Exception {
            when(sportProfileService.updateProfile(eq(1L), any(), any(), any()))
                .thenThrow(new UserNotFoundException("Sport profile not found"));
            mockMvc.perform(put("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
        }
        @Test void mustReturn400WhenRequestBodyIsEmpty() throws Exception {
            mockMvc.perform(put("/api/users/1/sport-profile").contentType(MediaType.APPLICATION_JSON)
                    .content("{}")).andExpect(status().isBadRequest());
        }
    }

    @Nested @DisplayName("DELETE /api/users/{id}/sport-profile")
    class DeleteSportProfileTests {
        @Test void mustReturn405WhenAttemptingToDeleteSportProfile() throws Exception {
            doThrow(new DeleteSportProfileNotAllowedException("Deleting a sport profile is not allowed"))
                .when(sportProfileService).deleteProfile(any());
            mockMvc.perform(delete("/api/users/1/sport-profile")).andExpect(status().isMethodNotAllowed());
        }
        @Test void mustReturn405RegardlessOfWhetherProfileExists() throws Exception {
            doThrow(new DeleteSportProfileNotAllowedException("Deleting a sport profile is not allowed"))
                .when(sportProfileService).deleteProfile(any());
            mockMvc.perform(delete("/api/users/99/sport-profile")).andExpect(status().isMethodNotAllowed());
        }
    }
}
