package project.demoChat.features.chat.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.domain.Message;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.chat.MessageRepository;
import project.demoChat.config.ChatSecurity;
import project.demoChat.features.chat.websocket.ChatEventPublisher;

@RequiredArgsConstructor
@Service
public class DeleteMessage {

    private final MessageRepository messageRepository;
    private final ChatSecurity chatSecurity;
    private final ChatEventPublisher chatPublisher;

    @Transactional
    public void execute(Long messageId, String username) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "メッセージが見つかりません"
                ));

        chatSecurity.checkMessageDelete(message, username);

        messageRepository.delete(message);

        chatPublisher.publishDelete(message);
    }
}