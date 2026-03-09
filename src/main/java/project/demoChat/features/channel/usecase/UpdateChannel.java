package project.demoChat.features.channel.usecase;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.demoChat.config.ChatSecurity;
import project.demoChat.domain.Channel;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.channel.ChannelRepository;
import project.demoChat.features.channel.websocket.ChannelEventPublisher;

@RequiredArgsConstructor
@Component
public class UpdateChannel {

    private final ChannelRepository channelRepository;
    private final ChatSecurity chatSecurity;
    private final ChannelEventPublisher channelPublisher;

    @Transactional
    public void execute(Long channelId, String newName, String username) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "チャンネルが見つかりません"
                ));

        chatSecurity.checkRole(channel.getServer().getId(), username, MemberRole.OWNER);

        channel.setName(newName);
        Channel savedChannel = channelRepository.save(channel);

        channelPublisher.publishUpdate(savedChannel);
    }
}