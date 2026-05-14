package edu.eci.userService.controller;

import edu.eci.userService.dto.SendJoinRequestRequest;
import edu.eci.userService.model.JoinRequest;
import edu.eci.userService.services.JoinRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for JoinRequest management.
 */
@RestController
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    public JoinRequestController(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
    }

    /** Player sends a join request to a team. */
    @PostMapping("/api/users/{playerId}/join-requests")
    public ResponseEntity<JoinRequest> sendRequest(@PathVariable Long playerId,
                                                    @Valid @RequestBody SendJoinRequestRequest request) {
        JoinRequest result = joinRequestService.sendRequest(playerId, request.getTeamId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /** Captain accepts a pending request directed to their team. */
    @PatchMapping("/api/teams/{teamId}/join-requests/{requestId}/accept")
    public ResponseEntity<JoinRequest> acceptRequest(@PathVariable Long requestId,
                                                      @PathVariable Long teamId) {
        return ResponseEntity.ok(joinRequestService.acceptRequest(requestId, teamId));
    }

    /** Captain rejects a pending request directed to their team. */
    @PatchMapping("/api/teams/{teamId}/join-requests/{requestId}/reject")
    public ResponseEntity<JoinRequest> rejectRequest(@PathVariable Long requestId,
                                                      @PathVariable Long teamId) {
        return ResponseEntity.ok(joinRequestService.rejectRequest(requestId, teamId));
    }

    /** List all pending join requests for a given team. */
    @GetMapping("/api/teams/{teamId}/join-requests")
    public ResponseEntity<List<JoinRequest>> listPendingRequests(@PathVariable Long teamId) {
        return ResponseEntity.ok(joinRequestService.listPendingRequests(teamId));
    }

    /** List all join requests sent by a specific player. */
    @GetMapping("/api/users/{playerId}/join-requests")
    public ResponseEntity<List<JoinRequest>> listByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(joinRequestService.findRequestsByPlayer(playerId));
    }

    /** Find a specific join request by ID. */
    @GetMapping("/api/join-requests/{requestId}")
    public ResponseEntity<JoinRequest> findById(@PathVariable Long requestId) {
        return ResponseEntity.ok(joinRequestService.findById(requestId));
    }
}
