package project.demoChat.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.config.RolePermission;
import project.demoChat.model.Message;
import project.demoChat.repository.MessageRepository;
import project.demoChat.websocket.MessageUpdatePublisher;

@RequiredArgsConstructor
@Service
public class UpdateMessage {

    private final RolePermission rolePermission;
    private final MessageRepository messageRepository;
    private final MessageUpdatePublisher messageUpdate;

    @Transactional
    public void execute(Long messageId, String content, String username) {
        Message newMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("メッセージが見つかりません"));

        rolePermission.checkMessageEdit(newMessage, username);

        newMessage.setContent(content);
        Message savedMessage = messageRepository.save(newMessage);

        messageUpdate.broadcastMessageUpdated(savedMessage);
    }
}