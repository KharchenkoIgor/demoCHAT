package project.demoChat.features.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.features.chat.usecase.CreateMessage;
import project.demoChat.features.chat.usecase.DeleteMessage;
import project.demoChat.features.chat.usecase.UpdateMessage;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final CreateMessage createMessage;
    private final DeleteMessage deleteMessage;
    private final UpdateMessage updateMessage;

    public void processAndBroadcast(MessageDTO messageDTO) {
        createMessage.execute(messageDTO);
    }

    public void deleteMessage(Long id, String username) {
        deleteMessage.execute(id, username);
    }

    public void updateMessage(Long id, String content, String username) {
        updateMessage.execute(id, content, username);
    }
}