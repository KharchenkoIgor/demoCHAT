package project.demoChat.features.chat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.demoChat.common.ServerEventNotifier;
import project.demoChat.domain.Message;
import project.demoChat.features.chat.MessageDTO;
import project.demoChat.features.chat.MessageMapper;

@RequiredArgsConstructor
@Component
public class ChatEventPublisher {
    private final ServerEventNotifier notifier;
    private final MessageMapper mapper;

    public void publishCreate(MessageDTO dto) {
        dto.setType("MESSAGE");
        notifier.broadcastToEntriesServer(dto.getServerId(), dto);
    }

    public void publishUpdate(Message message) {
        MessageDTO dto = mapper.toDTO(message);
        dto.setType("UPDATE_MESSAGE");
        notifier.broadcastToEntriesServer(message.getChannel().getServer().getId(), dto);
    }

    public void publishDelete(Message message) {
        MessageDTO dto = mapper.toDTO(message);
        dto.setType("DELETE_MESSAGE");
        notifier.broadcastToEntriesServer(message.getChannel().getServer().getId(), dto);
    }
}