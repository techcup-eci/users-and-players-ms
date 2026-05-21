package edu.eci.userService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.eci.userService.dto.JoinRequestDTO;
import edu.eci.userService.dto.SendJoinRequestRequest;
import edu.eci.userService.exception.PendingJoinRequestException;
import edu.eci.userService.exception.TeamNotAvailableException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.services.JoinRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/join-requests")
@Tag(name = "Join Requests", description = "Endpoints for managing player join requests to teams")
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    public JoinRequestController(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
    }

    @PostMapping("/players/{playerId}/send")
    @Operation(summary = "Send a join request",
               description = "A player sends a request to join a team")
    public ResponseEntity<JoinRequestDTO> sendJoinRequest(
            @PathVariable Long playerId,
            @RequestBody SendJoinRequestRequest request) {
        try {
            JoinRequestDTO result = joinRequestService.sendRequest(playerId, request.getTeamId());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (PendingJoinRequestException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (TeamNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/{requestId}/accept")
    @Operation(summary = "Accept a join request",
               description = "A captain accepts a pending join request for their team")
    public ResponseEntity<JoinRequestDTO> acceptJoinRequest(
            @PathVariable Long requestId,
            @RequestParam Long teamId) {
        try {
            JoinRequestDTO result = joinRequestService.acceptRequest(requestId, teamId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PatchMapping("/{requestId}/reject")
    @Operation(summary = "Reject a join request",
               description = "A captain rejects a pending join request for their team")
    public ResponseEntity<JoinRequestDTO> rejectJoinRequest(
            @PathVariable Long requestId,
            @RequestParam Long teamId) {
        try {
            JoinRequestDTO result = joinRequestService.rejectRequest(requestId, teamId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get a join request",
               description = "Retrieve a specific join request by ID")
    public ResponseEntity<JoinRequestDTO> getJoinRequest(@PathVariable Long requestId) {
        try {
            JoinRequestDTO result = joinRequestService.getRequestById(requestId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/teams/{teamId}")
    @Operation(summary = "List team requests",
               description = "Get all pending join requests for a team")
    public ResponseEntity<List<JoinRequestDTO>> getTeamRequests(@PathVariable Long teamId) {
        List<JoinRequestDTO> requests = joinRequestService.getRequestsByTeam(teamId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/players/{playerId}")
    @Operation(summary = "List player requests",
               description = "Get all join requests sent by a player")
    public ResponseEntity<List<JoinRequestDTO>> getPlayerRequests(@PathVariable Long playerId) {
        List<JoinRequestDTO> requests = joinRequestService.getRequestsByPlayer(playerId);
        return ResponseEntity.ok(requests);
    }
}
