package project.demoChat.features.chat.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.domain.Message;
import project.demoChat.features.chat.MessageRepository;
import project.demoChat.config.ChatSecurity;
import project.demoChat.features.chat.websocket.DeleteMessagePublisher;

@RequiredArgsConstructor
@Service
public class DeleteMessage {

    private final MessageRepository messageRepository;
    private final ChatSecurity chatSecurity;
    private final DeleteMessagePublisher deleteMessagePublisher;

    @Transactional
    public void execute(Long messageId, String username) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        chatSecurity.checkMessageDelete(message, username);

        deleteMessagePublisher.broadcastMessageDelete(message);

        messageRepository.delete(message);
    }
}