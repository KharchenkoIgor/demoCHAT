package project.demoChat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import project.demoChat.DTO.JoinRequestDTO;
import project.demoChat.DTO.ServerDTO;
import project.demoChat.mapper.JoinRequestMapper;
import project.demoChat.mapper.ServerMapper;
import project.demoChat.model.Server;
import project.demoChat.service.ServerService;
import project.demoChat.config.ChatSecurity;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/servers")
public class ServerController {

    private final ServerService serverService;
    private final ChatSecurity chatSecurity;
    private final ServerMapper serverMapper;
    private final JoinRequestMapper joinRequestMapper;

    @PostMapping
    public ServerDTO createServer(@RequestBody ServerDTO serverDTO, Principal principal) {
        Server savedServer = serverService.createServer(
                serverDTO.getName(),
                serverDTO.isPublicStatus(),
                principal.getName()
        );

        return serverMapper.toDTO(savedServer);
    }

    @GetMapping
    public List<ServerDTO> getMyServers(Principal principal) {
        return serverService.getUserServers(principal.getName())
                .stream()
                .map(serverMapper::toDTO)
                .toList();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateServer(@PathVariable Long id,
                                          @RequestBody Map<String, String> body,
                                          Principal principal) {
        serverService.updateServer(id, body.get("name"), principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteServer(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        serverService.deleteServer(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Map<String, String>> joinServer(@PathVariable Long id, Principal principal) {
        String result = serverService.joinServer(id, principal.getName());
        return ResponseEntity.ok(Map.of("status", result));
    }

    @GetMapping("/search")
    public List<ServerDTO> searchServers(@RequestParam String name) {
        return serverService.searchServer(name)
                .stream()
                .map(serverMapper::toDTO)
                .toList();
    }

    @GetMapping("/{serverId}/requests")
    public List<JoinRequestDTO> getPendingRequests(@PathVariable Long serverId, Principal principal) {
        return serverService.getPendingRequests(serverId, principal.getName())
                .stream()
                .map(joinRequestMapper::toDTO)
                .toList();
    }

    @PostMapping("/requests/{requestId}/accepted")
    public ResponseEntity<?> acceptedJoinRequest(@PathVariable Long requestId, Principal principal) {
        serverService.acceptedJoinRequest(requestId, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<?> rejectJoinRequest(@PathVariable Long requestId, Principal principal) {
        serverService.rejectJoinRequest(requestId,principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{serverId}/my-role")
    public ResponseEntity<Map<String, String>> getMyRole(@PathVariable Long serverId, Principal principal) {
        String role = chatSecurity.getUserRole(serverId, principal.getName());
        if ("NONE".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(Map.of("role", role));
    }
}