package edu.eci.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.userService.dto.SendJoinRequestRequest;
import edu.eci.userService.dto.JoinRequestDTO;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.exception.PendingJoinRequestException;
import edu.eci.userService.exception.TeamNotAvailableException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.enums.JoinRequestStatus;
import edu.eci.userService.services.JoinRequestService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * REST layer tests for JoinRequestController.
 *
 * Architecture note:
 * This microservice runs behind the Orchestrator (API Gateway).
 * The gateway handles token validation and blocks unauthenticated requests.
 * @AutoConfigureMockMvc(addFilters = false) disables Spring Security filters
 * locally since token validation is not the responsibility of this microservice.
 *
 * Endpoints:
 *   POST   /api/users/{playerId}/join-requests               A player sends a request to a team
 *   PATCH  /api/teams/{teamId}/join-requests/{requestId}/accept  Captain accepts a request
 *   PATCH  /api/teams/{teamId}/join-requests/{requestId}/reject  Captain rejects a request
 *   GET    /api/teams/{teamId}/join-requests                 Captain lists pending requests
 *   GET    /api/users/{playerId}/join-requests               Player lists their own requests
 *   GET    /api/join-requests/{requestId}                    Find a request by ID
 */
@WebMvcTest(JoinRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("JoinRequestController - REST Tests")
class JoinRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JoinRequestService joinRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO player;
    private JoinRequestDTO pendingRequest;
    private JoinRequestDTO acceptedRequest;
    private JoinRequestDTO rejectedRequest;
    private SendJoinRequestRequest validSendRequest;

    @BeforeEach
    void setUp() {
        player = new UserDTO();
        player.setId(1L);
        player.setFullName("Luis Martinez");

        pendingRequest = new JoinRequestDTO();
        pendingRequest.setId(100L);
        pendingRequest.setPlayer(player);
        pendingRequest.setTeamId(5L);
        pendingRequest.setStatus(JoinRequestStatus.PENDING);

        acceptedRequest = new JoinRequestDTO();
        acceptedRequest.setId(100L);
        acceptedRequest.setPlayer(player);
        acceptedRequest.setTeamId(5L);
        acceptedRequest.setStatus(JoinRequestStatus.ACCEPTED);

        rejectedRequest = new JoinRequestDTO();
        rejectedRequest.setId(100L);
        rejectedRequest.setPlayer(player);
        rejectedRequest.setTeamId(5L);
        rejectedRequest.setStatus(JoinRequestStatus.REJECTED);

        validSendRequest = new SendJoinRequestRequest();
        validSendRequest.setTeamId(5L);
    }

    // ----------------------------------------------------------------
    // POST /api/users/{playerId}/join-requests
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("POST /api/users/{playerId}/join-requests - Send join request")
    class SendJoinRequestTests {

        @Test
        @DisplayName("Must return 201 when request is created successfully")
        void mustReturn201WhenRequestIsCreatedSuccessfully() throws Exception {
            when(joinRequestService.sendRequest(1L, 5L)).thenReturn(pendingRequest);

            mockMvc.perform(post("/join-requests/players/1/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.teamId").value(5L));
        }

        @Test
        @DisplayName("Must return 409 when player already has a pending request")
        void mustReturn409WhenPlayerAlreadyHasPendingRequest() throws Exception {
            when(joinRequestService.sendRequest(1L, 5L))
                .thenThrow(new PendingJoinRequestException("Player already has a pending request"));

            mockMvc.perform(post("/join-requests/players/1/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Must return 404 when player does not exist")
        void mustReturn404WhenPlayerDoesNotExist() throws Exception {
            when(joinRequestService.sendRequest(99L, 5L))
                .thenThrow(new UserNotFoundException("Player not found"));

            mockMvc.perform(post("/join-requests/players/99/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Must return 400 when team is not available")
        void mustReturn400WhenTeamIsNotAvailable() throws Exception {
            when(joinRequestService.sendRequest(1L, 5L))
                .thenThrow(new TeamNotAvailableException("Team is not available"));

            mockMvc.perform(post("/join-requests/players/1/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    // ----------------------------------------------------------------
    // PATCH /join-requests/{requestId}/accept
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("PATCH /join-requests/{requestId}/accept - Accept request")
    class AcceptJoinRequestTests {

        @Test
        @DisplayName("Must return 200 with ACCEPTED status when captain accepts a pending request")
        void mustReturn200WithAcceptedStatusWhenCaptainAcceptsPendingRequest() throws Exception {
            when(joinRequestService.acceptRequest(100L, 5L)).thenReturn(acceptedRequest);

            mockMvc.perform(patch("/join-requests/100/accept?teamId=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
        }

        @Test
        @DisplayName("Must return 409 when request is no longer pending")
        void mustReturn409WhenRequestIsNoLongerPending() throws Exception {
            when(joinRequestService.acceptRequest(100L, 5L))
                .thenThrow(new IllegalStateException("Request is not pending and cannot be accepted"));

            mockMvc.perform(patch("/join-requests/100/accept?teamId=5"))
                .andExpect(status().isConflict());
        }
    }

    // ----------------------------------------------------------------
    // PATCH /join-requests/{requestId}/reject
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("PATCH /join-requests/{requestId}/reject - Reject request")
    class RejectJoinRequestTests {

        @Test
        @DisplayName("Must return 200 with REJECTED status when captain rejects a pending request")
        void mustReturn200WithRejectedStatusWhenCaptainRejectsPendingRequest() throws Exception {
            when(joinRequestService.rejectRequest(100L, 5L)).thenReturn(rejectedRequest);

            mockMvc.perform(patch("/join-requests/100/reject?teamId=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("REJECTED"));
        }

        @Test
        @DisplayName("Must return 409 when request is no longer pending")
        void mustReturn409WhenRequestIsNoLongerPending() throws Exception {
            when(joinRequestService.rejectRequest(100L, 5L))
                .thenThrow(new IllegalStateException("Request is not pending and cannot be rejected"));

            mockMvc.perform(patch("/api/teams/5/join-requests/100/reject"))
                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Must return 400 when request does not belong to the captain team")
        void mustReturn400WhenRequestDoesNotBelongToCaptainTeam() throws Exception {
            when(joinRequestService.rejectRequest(100L, 8L))
                .thenThrow(new IllegalArgumentException("Request does not belong to this team"));

            mockMvc.perform(patch("/join-requests/100/reject?teamId=5"))
                .andExpect(status().isConflict());
        }
    }

    // ----------------------------------------------------------------
    // GET /join-requests/teams/{teamId}
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("GET /join-requests/teams/{teamId} - List pending requests for team")
    class ListPendingRequestsForTeamTests {

        @Test
        @DisplayName("Must return 200 with list of pending requests for the team")
        void mustReturn200WithListOfPendingRequestsForTeam() throws Exception {
            when(joinRequestService.getRequestsByTeam(5L)).thenReturn(List.of(pendingRequest));

            mockMvc.perform(get("/join-requests/teams/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
        }
    }

    // ----------------------------------------------------------------
    // GET /join-requests/players/{playerId}
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("GET /join-requests/players/{playerId} - List requests by player")
    class ListRequestsByPlayerTests {

        @Test
        @DisplayName("Must return 200 with all requests sent by the player")
        void mustReturn200WithAllRequestsSentByPlayer() throws Exception {
            when(joinRequestService.getRequestsByPlayer(1L)).thenReturn(List.of(pendingRequest));

            mockMvc.perform(get("/join-requests/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
        }
    }

    // ----------------------------------------------------------------
    // GET /join-requests/{requestId}
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("GET /join-requests/{requestId} - Get request by ID")
    class GetRequestByIdTests {

        @Test
        @DisplayName("Must return 200 with request data when it exists")
        void mustReturn200WithRequestDataWhenItExists() throws Exception {
            when(joinRequestService.getRequestById(100L)).thenReturn(pendingRequest);

            mockMvc.perform(get("/join-requests/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("PENDING"));
        }
    }
}
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Must return 400 when request ID in URL is not numeric")
        void mustReturn400WhenRequestIdIsNotNumeric() throws Exception {
            mockMvc.perform(get("/api/join-requests/abc"))
                .andExpect(status().isBadRequest());
        }
    }
}
