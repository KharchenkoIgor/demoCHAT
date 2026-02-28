package project.demoChat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.util.List;
import java.util.Map;
import java.security.Principal;

import project.demoChat.DTO.MessageDTO;
import project.demoChat.mapper.MessageMapper;
import project.demoChat.service.MessageService;
import project.demoChat.repository.MessageRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class ChatController {

    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @MessageMapping("/send")
    public void handleChatMessage(MessageDTO messageDTO, Principal principal) {
        messageDTO.setSenderName(principal.getName());
        messageDTO.setType("MESSAGE");
        messageService.processAndBroadcast(messageDTO);
    }

    @GetMapping("/channel/{channelId}")
    public List<MessageDTO> getHistory(@PathVariable Long channelId) {
        return messageRepository.findByChannelIdOrderByCreatedAt(channelId)
                .stream()
                .map(messageMapper::toDTO)
                .toList();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
        messageService.updateMessage(id, body.get("content"), principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        messageService.deleteMessage(id, user.getUsername());
        return ResponseEntity.ok().build();
    }
}