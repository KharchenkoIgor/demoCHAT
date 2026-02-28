package project.demoChat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import project.demoChat.model.Channel;
import project.demoChat.service.ChannelService;
import project.demoChat.DTO.ChannelDTO;
import project.demoChat.config.ChatSecurity;
import project.demoChat.mapper.ChannelMapper;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;
    private final ChatSecurity chatSecurity;
    private final ChannelMapper channelMapper;

    @PostMapping
    public ChannelDTO createChannel(@RequestBody ChannelDTO channelDTO, Principal principal) {

        chatSecurity.checkMembership(channelDTO.getServerId(), principal.getName());

        Channel savedChannel = channelService.createChannel(channelDTO);

        return channelMapper.toDTO(savedChannel);
    }

    @GetMapping("/server/{serverId}")
    public List<ChannelDTO> getChannels(@PathVariable Long serverId, Principal principal) {
        chatSecurity.checkMembership(serverId, principal.getName());

        return channelService.getChannelsByServer(serverId)
                .stream()
                .map(channelMapper::toDTO)
                .toList();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateChannel(@PathVariable Long id,
                                           @RequestBody Map<String, String> body,
                                           Principal principal) {
        channelService.updateChannel(id, body.get("name"), principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChannel(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        channelService.deleteChannel(id, user.getUsername());
        return ResponseEntity.ok().build();
    }
}