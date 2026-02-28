package project.demoChat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.demoChat.DTO.MessageDTO;
import project.demoChat.model.*;
import project.demoChat.repository.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CreateMessage {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public MessageDTO execute(MessageDTO messageDTO) {
        User author = userRepository.findByUsername(messageDTO.getSenderName())
                .orElseThrow(() -> new RuntimeException("ユーザー名が見つかりません"));

        Channel channel = channelRepository.findById(messageDTO.getChannelId())
                .orElseThrow(() -> new RuntimeException("チャンネル名が見つかりません"));

        Message newMessage = new Message();
        newMessage.setContent(messageDTO.getContent());
        newMessage.setAuthor(author);
        newMessage.setChannel(channel);
        newMessage.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(newMessage);

        messageDTO.setId(savedMessage.getId());
        return messageDTO;
    }
}