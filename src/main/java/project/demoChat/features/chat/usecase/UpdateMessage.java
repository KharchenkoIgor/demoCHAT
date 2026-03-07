package project.demoChat.features.chat.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.config.ChatSecurity;
import project.demoChat.domain.Message;
import project.demoChat.features.chat.MessageRepository;
import project.demoChat.features.chat.websocket.MessageUpdatePublisher;

@RequiredArgsConstructor
@Service
public class UpdateMessage {

    private final ChatSecurity chatSecurity;
    private final MessageRepository messageRepository;
    private final MessageUpdatePublisher messageUpdate;

    @Transactional
    public void execute(Long messageId, String content, String username) {
        Message newMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("メッセージが見つかりません"));

        chatSecurity.checkMessageEdit(newMessage, username);

        newMessage.setContent(content);
        Message savedMessage = messageRepository.save(newMessage);

        messageUpdate.broadcastMessageUpdated(savedMessage);
    }
}