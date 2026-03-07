package project.demoChat.features.chat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.demoChat.features.auth.UserRepository;
import project.demoChat.features.channel.ChannelRepository;
import project.demoChat.features.chat.MessageDTO;
import project.demoChat.domain.*;
import project.demoChat.features.chat.MessageRepository;

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