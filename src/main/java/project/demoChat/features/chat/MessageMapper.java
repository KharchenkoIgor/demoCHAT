package project.demoChat.features.chat;

import org.springframework.stereotype.Component;
import project.demoChat.domain.Message;

@Component
public class MessageMapper {

    public MessageDTO toDTO(Message message) {

        MessageDTO dto = new MessageDTO();

        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSenderName(message.getAuthor().getUsername());
        dto.setChannelId(message.getChannel().getId());
        dto.setServerId(message.getChannel().getServer().getId());
        dto.setCreatedAt(message.getCreatedAt().toString());
        dto.setType("MESSAGE");

        return dto;
    }
}