package project.demoChat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.DTO.MessageDTO;
import project.demoChat.usecase.*;
import project.demoChat.usecase.UpdateMessage;
import project.demoChat.websocket.BroadcastMessage;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final CreateMessage createMessage;
    private final BroadcastMessage broadcastMessage;
    private final DeleteMessage deleteMessage;
    private final UpdateMessage updateMessage;

    public void processAndBroadcast(MessageDTO messageDTO) {

        MessageDTO savedMessageDTO = createMessage.execute(messageDTO);

        broadcastMessage.execute(savedMessageDTO);
    }

    public void deleteMessage(Long id, String username) {
        deleteMessage.execute(id, username);
    }

    public void updateMessage(Long id, String content, String username) {
        updateMessage.execute(id, content, username);
    }
}