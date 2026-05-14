package edu.eci.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.userService.dto.SendJoinRequestRequest;
import edu.eci.userService.exception.PendingJoinRequestException;
import edu.eci.userService.exception.TeamNotAvailableException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.model.JoinRequest;
import edu.eci.userService.model.User;
import edu.eci.userService.model.enums.JoinRequestStatus;
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

@WebMvcTest(controllers = {JoinRequestController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("JoinRequestController - REST Tests")
class JoinRequestControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private JoinRequestService joinRequestService;
    @Autowired private ObjectMapper objectMapper;

    private User player;
    private JoinRequest pendingRequest;
    private JoinRequest acceptedRequest;
    private JoinRequest rejectedRequest;
    private SendJoinRequestRequest validSendRequest;

    @BeforeEach
    void setUp() {
        player = new User();
        player.setId(1L);
        player.setFullName("Luis Martinez");

        pendingRequest = new JoinRequest();
        pendingRequest.setId(100L);
        pendingRequest.setPlayer(player);
        pendingRequest.setTeamId(5L);
        pendingRequest.setStatus(JoinRequestStatus.PENDING);

        acceptedRequest = new JoinRequest();
        acceptedRequest.setId(100L);
        acceptedRequest.setPlayer(player);
        acceptedRequest.setTeamId(5L);
        acceptedRequest.setStatus(JoinRequestStatus.ACCEPTED);

        rejectedRequest = new JoinRequest();
        rejectedRequest.setId(100L);
        rejectedRequest.setPlayer(player);
        rejectedRequest.setTeamId(5L);
        rejectedRequest.setStatus(JoinRequestStatus.REJECTED);

        validSendRequest = new SendJoinRequestRequest();
        validSendRequest.setTeamId(5L);
    }

    @Nested @DisplayName("POST /api/users/{playerId}/join-requests")
    class SendJoinRequestTests {
        @Test void mustReturn201WhenRequestIsCreatedSuccessfully() throws Exception {
            when(joinRequestService.sendRequest(eq(1L), eq(5L))).thenReturn(pendingRequest);
            mockMvc.perform(post("/api/users/1/join-requests").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.teamId").value(5L));
        }
        @Test void mustReturn409WhenPlayerAlreadyHasPendingRequest() throws Exception {
            when(joinRequestService.sendRequest(eq(1L), anyLong()))
                .thenThrow(new PendingJoinRequestException("Player already has a pending request"));
            mockMvc.perform(post("/api/users/1/join-requests").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isConflict());
        }
        @Test void mustReturn404WhenPlayerDoesNotExist() throws Exception {
            when(joinRequestService.sendRequest(eq(99L), anyLong()))
                .thenThrow(new UserNotFoundException("Player not found"));
            mockMvc.perform(post("/api/users/99/join-requests").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isNotFound());
        }
        @Test void mustReturn400WhenTeamIdIsNullInRequestBody() throws Exception {
            validSendRequest.setTeamId(null);
            mockMvc.perform(post("/api/users/1/join-requests").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isBadRequest());
        }
        @Test void mustReturn400WhenTeamIsNotAvailable() throws Exception {
            when(joinRequestService.sendRequest(eq(1L), eq(5L)))
                .thenThrow(new TeamNotAvailableException("Team is not available"));
            mockMvc.perform(post("/api/users/1/join-requests").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isBadRequest());
        }
        @Test void mustReturn400WhenPlayerIdIsNotNumeric() throws Exception {
            mockMvc.perform(post("/api/users/abc/join-requests").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSendRequest)))
                .andExpect(status().isBadRequest());
        }
        @Test void mustReturn400WhenRequestBodyIsEmpty() throws Exception {
            mockMvc.perform(post("/api/users/1/join-requests").contentType(MediaType.APPLICATION_JSON)
                    .content("{}")).andExpect(status().isBadRequest());
        }
    }

    @Nested @DisplayName("PATCH /api/teams/{teamId}/join-requests/{requestId}/accept")
    class AcceptJoinRequestTests {
        @Test void mustReturn200WithAcceptedStatusWhenCaptainAcceptsPendingRequest() throws Exception {
            when(joinRequestService.acceptRequest(100L, 5L)).thenReturn(acceptedRequest);
            mockMvc.perform(patch("/api/teams/5/join-requests/100/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
        }
        @Test void mustReturn409WhenRequestIsNoLongerPending() throws Exception {
            when(joinRequestService.acceptRequest(100L, 5L))
                .thenThrow(new IllegalStateException("Request is not pending"));
            mockMvc.perform(patch("/api/teams/5/join-requests/100/accept")).andExpect(status().isConflict());
        }
        @Test void mustReturn400WhenRequestDoesNotBelongToCaptainTeam() throws Exception {
            when(joinRequestService.acceptRequest(100L, 9L))
                .thenThrow(new IllegalArgumentException("Request does not belong to this team"));
            mockMvc.perform(patch("/api/teams/9/join-requests/100/accept")).andExpect(status().isBadRequest());
        }
        @Test void mustReturn404WhenRequestDoesNotExist() throws Exception {
            when(joinRequestService.acceptRequest(999L, 5L))
                .thenThrow(new UserNotFoundException("Request not found"));
            mockMvc.perform(patch("/api/teams/5/join-requests/999/accept")).andExpect(status().isNotFound());
        }
        @Test void mustReturn400WhenRequestIdIsNotNumeric() throws Exception {
            mockMvc.perform(patch("/api/teams/5/join-requests/abc/accept")).andExpect(status().isBadRequest());
        }
    }

    @Nested @DisplayName("PATCH /api/teams/{teamId}/join-requests/{requestId}/reject")
    class RejectJoinRequestTests {
        @Test void mustReturn200WithRejectedStatusWhenCaptainRejectsPendingRequest() throws Exception {
            when(joinRequestService.rejectRequest(100L, 5L)).thenReturn(rejectedRequest);
            mockMvc.perform(patch("/api/teams/5/join-requests/100/reject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("REJECTED"));
        }
        @Test void mustReturn409WhenRequestIsNoLongerPending() throws Exception {
            when(joinRequestService.rejectRequest(100L, 5L))
                .thenThrow(new IllegalStateException("Request is not pending"));
            mockMvc.perform(patch("/api/teams/5/join-requests/100/reject")).andExpect(status().isConflict());
        }
        @Test void mustReturn400WhenRequestDoesNotBelongToCaptainTeam() throws Exception {
            when(joinRequestService.rejectRequest(100L, 8L))
                .thenThrow(new IllegalArgumentException("Request does not belong to this team"));
            mockMvc.perform(patch("/api/teams/8/join-requests/100/reject")).andExpect(status().isBadRequest());
        }
        @Test void mustReturn404WhenRequestDoesNotExist() throws Exception {
            when(joinRequestService.rejectRequest(999L, 5L))
                .thenThrow(new UserNotFoundException("Request not found"));
            mockMvc.perform(patch("/api/teams/5/join-requests/999/reject")).andExpect(status().isNotFound());
        }
        @Test void mustReturn400WhenTeamIdIsNotNumeric() throws Exception {
            mockMvc.perform(patch("/api/teams/abc/join-requests/100/reject")).andExpect(status().isBadRequest());
        }
    }

    @Nested @DisplayName("GET /api/teams/{teamId}/join-requests")
    class ListPendingRequestsForTeamTests {
        @Test void mustReturn200WithListOfPendingRequestsForTeam() throws Exception {
            when(joinRequestService.listPendingRequests(5L)).thenReturn(List.of(pendingRequest));
            mockMvc.perform(get("/api/teams/5/join-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].teamId").value(5L));
        }
        @Test void mustReturn200WithEmptyArrayWhenTeamHasNoPendingRequests() throws Exception {
            when(joinRequestService.listPendingRequests(5L)).thenReturn(List.of());
            mockMvc.perform(get("/api/teams/5/join-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        }
        @Test void mustReturn400WhenTeamIdIsNotNumeric() throws Exception {
            mockMvc.perform(get("/api/teams/abc/join-requests")).andExpect(status().isBadRequest());
        }
    }

    @Nested @DisplayName("GET /api/users/{playerId}/join-requests")
    class ListRequestsByPlayerTests {
        @Test void mustReturn200WithAllRequestsSentByPlayer() throws Exception {
            when(joinRequestService.findRequestsByPlayer(1L)).thenReturn(List.of(pendingRequest));
            mockMvc.perform(get("/api/users/1/join-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].teamId").value(5L));
        }
        @Test void mustReturn200WithEmptyArrayWhenPlayerHasNoRequests() throws Exception {
            when(joinRequestService.findRequestsByPlayer(1L)).thenReturn(List.of());
            mockMvc.perform(get("/api/users/1/join-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        }
        @Test void mustReturn400WhenPlayerIdIsNotNumeric() throws Exception {
            mockMvc.perform(get("/api/users/abc/join-requests")).andExpect(status().isBadRequest());
        }
    }

    @Nested @DisplayName("GET /api/join-requests/{requestId}")
    class FindRequestByIdTests {
        @Test void mustReturn200WithRequestDataWhenItExists() throws Exception {
            when(joinRequestService.findById(100L)).thenReturn(pendingRequest);
            mockMvc.perform(get("/api/join-requests/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.teamId").value(5L));
        }
        @Test void mustReturn404WhenRequestDoesNotExist() throws Exception {
            when(joinRequestService.findById(999L)).thenThrow(new UserNotFoundException("Request not found"));
            mockMvc.perform(get("/api/join-requests/999")).andExpect(status().isNotFound());
        }
        @Test void mustReturn400WhenRequestIdIsNotNumeric() throws Exception {
            mockMvc.perform(get("/api/join-requests/abc")).andExpect(status().isBadRequest());
        }
    }
}
