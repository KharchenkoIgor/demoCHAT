package project.demoChat.features.chat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.auth.UserRepository;
import project.demoChat.features.channel.ChannelRepository;
import project.demoChat.features.chat.MessageDTO;
import project.demoChat.domain.*;
import project.demoChat.features.chat.MessageRepository;
import project.demoChat.features.chat.websocket.ChatEventPublisher;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CreateMessage {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ChatEventPublisher chatPublisher;

    @Transactional
    public MessageDTO execute(MessageDTO messageDTO) {
        User author = userRepository.findByUsername(messageDTO.getSenderName())
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "ユーザー名が見つかりません"
                ));

        Channel channel = channelRepository.findById(messageDTO.getChannelId())
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "チャンネル名が見つかりません"
                ));

        Message newMessage = new Message();
        newMessage.setContent(messageDTO.getContent());
        newMessage.setAuthor(author);
        newMessage.setChannel(channel);
        newMessage.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(newMessage);

        messageDTO.setId(savedMessage.getId());

        chatPublisher.publishCreate(messageDTO);

        return messageDTO;
    }
}