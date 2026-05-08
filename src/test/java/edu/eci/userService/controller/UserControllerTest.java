package edu.eci.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.userService.dto.UpdateUserRequest;
import edu.eci.userService.exception.UserLinkedToActiveTournamentException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.model.User;
import edu.eci.userService.model.enums.SchoolRelation;
import edu.eci.userService.model.enums.UserStatus;
import edu.eci.userService.service.UserService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * REST layer tests for UserController.
 *
 * Architecture note:
 * This microservice runs behind the Orchestrator (API Gateway).
 * The gateway is responsible for blocking unauthenticated requests (401)
 * and performing basic JWT validation before routing the request here.
 * For that reason, @AutoConfigureMockMvc(addFilters = false) is used to
 * disable Spring Security filters locally. Token validation is not the
 * responsibility of this microservice.
 *
 * What is tested here:
 *   - Correct HTTP status codes for different business scenarios
 *   - Input data validation (400 for invalid or missing fields)
 *   - Correct exception-to-HTTP-status mapping
 *   - Password field is never exposed in any response
 *
 * Endpoints:
 *   GET   /api/users/{id}
 *   PUT   /api/users/{id}
 *   PATCH /api/users/{id}/deactivate
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UserController - REST Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User baseUser;
    private UpdateUserRequest validRequest;

    @BeforeEach
    void setUp() {
        baseUser = new User();
        baseUser.setId(1L);
        baseUser.setFullName("Carlos Perez");
        baseUser.setEmail("carlos.perez@escuela.edu.co");
        baseUser.setSchoolRelation(SchoolRelation.STUDENT);
        baseUser.setAcademicProgram("Systems Engineering");
        baseUser.setSemester(5);
        baseUser.setStatus(UserStatus.ACTIVE);

        validRequest = new UpdateUserRequest();
        validRequest.setFullName("Carlos Alberto Perez");
        validRequest.setSchoolRelation(SchoolRelation.STUDENT);
        validRequest.setAcademicProgram("Systems Engineering");
        validRequest.setSemester(6);
    }

    // ----------------------------------------------------------------
    // GET /api/users/{id}
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("GET /api/users/{id} - Find user by ID")
    class FindUserByIdTests {

        @Test
        @DisplayName("Must return 200 with user data when user exists")
        void mustReturn200WithUserDataWhenUserExists() throws Exception {
            when(userService.findById(1L)).thenReturn(baseUser);

            mockMvc.perform(get("/api/users/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Carlos Perez"))
                .andExpect(jsonPath("$.email").value("carlos.perez@escuela.edu.co"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
        }

        @Test
        @DisplayName("Must return 404 when user does not exist")
        void mustReturn404WhenUserDoesNotExist() throws Exception {
            when(userService.findById(99L))
                .thenThrow(new UserNotFoundException("User not found"));

            mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Must never expose the password field in the response")
        void mustNeverExposePasswordFieldInResponse() throws Exception {
            when(userService.findById(1L)).thenReturn(baseUser);

            mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
        }

        @Test
        @DisplayName("Must return 400 when ID in URL is not a valid number")
        void mustReturn400WhenIdIsNotAValidNumber() throws Exception {
            mockMvc.perform(get("/api/users/abc"))
                .andExpect(status().isBadRequest());
        }
    }

    // ----------------------------------------------------------------
    // PUT /api/users/{id}
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("PUT /api/users/{id} - Update user")
    class UpdateUserTests {

        @Test
        @DisplayName("Must return 200 when update request contains valid data")
        void mustReturn200WhenUpdateRequestContainsValidData() throws Exception {
            baseUser.setFullName("Carlos Alberto Perez");
            when(userService.updateUser(eq(1L), any(), any(), any(), any()))
                .thenReturn(baseUser);

            mockMvc.perform(put("/api/users/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Carlos Alberto Perez"));
        }

        @Test
        @DisplayName("Must return 400 when full name is empty")
        void mustReturn400WhenFullNameIsEmpty() throws Exception {
            validRequest.setFullName("");

            mockMvc.perform(put("/api/users/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Must return 400 when full name is null")
        void mustReturn400WhenFullNameIsNull() throws Exception {
            validRequest.setFullName(null);

            mockMvc.perform(put("/api/users/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Must return 404 when user to update does not exist")
        void mustReturn404WhenUserToUpdateDoesNotExist() throws Exception {
            when(userService.updateUser(eq(99L), any(), any(), any(), any()))
                .thenThrow(new UserNotFoundException("User not found"));

            mockMvc.perform(put("/api/users/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Must return 400 when request body includes email field which is not updatable")
        void mustReturn400WhenRequestBodyIncludesEmailField() throws Exception {
            validRequest.setEmail("new@email.com");

            mockMvc.perform(put("/api/users/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Must return 400 when request body is empty")
        void mustReturn400WhenRequestBodyIsEmpty() throws Exception {
            mockMvc.perform(put("/api/users/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().isBadRequest());
        }
    }

    // ----------------------------------------------------------------
    // PATCH /api/users/{id}/deactivate
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("PATCH /api/users/{id}/deactivate - Deactivate user")
    class DeactivateUserTests {

        @Test
        @DisplayName("Must return 200 when user is successfully deactivated")
        void mustReturn200WhenUserIsSuccessfullyDeactivated() throws Exception {
            baseUser.setStatus(UserStatus.INACTIVE);
            when(userService.deactivateUser(1L)).thenReturn(baseUser);

            mockMvc.perform(patch("/api/users/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
        }

        @Test
        @DisplayName("Must return 409 when user is linked to an active or in-progress tournament")
        void mustReturn409WhenUserIsLinkedToActiveTournament() throws Exception {
            when(userService.deactivateUser(1L))
                .thenThrow(new UserLinkedToActiveTournamentException("Linked to active tournament"));

            mockMvc.perform(patch("/api/users/1/deactivate"))
                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Must return 404 when user to deactivate does not exist")
        void mustReturn404WhenUserToDeactivateDoesNotExist() throws Exception {
            when(userService.deactivateUser(99L))
                .thenThrow(new UserNotFoundException("User not found"));

            mockMvc.perform(patch("/api/users/99/deactivate"))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Must return 400 when ID in URL is not numeric")
        void mustReturn400WhenIdIsNotNumeric() throws Exception {
            mockMvc.perform(patch("/api/users/xyz/deactivate"))
                .andExpect(status().isBadRequest());
        }
    }
}
