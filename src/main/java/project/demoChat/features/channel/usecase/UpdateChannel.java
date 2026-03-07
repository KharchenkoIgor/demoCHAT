package project.demoChat.features.channel.usecase;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.demoChat.domain.Channel;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.channel.ChannelRepository;
import project.demoChat.config.RolePermission;
import project.demoChat.features.channel.websocket.ChannelUpdatePublisher;

@RequiredArgsConstructor
@Component
public class UpdateChannel {

    private final ChannelRepository channelRepository;
    private final RolePermission rolePermission;
    private final ChannelUpdatePublisher channelUpdatePublisher;

    @Transactional
    public void execute(Long channelId, String newName, String username) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "チャンネルが見つかりません"
                ));

        rolePermission.checkRole(channel.getServer().getId(), username, MemberRole.OWNER);

        channel.setName(newName);
        Channel savedChannel = channelRepository.save(channel);

        channelUpdatePublisher.broadcastChannelUpdated(savedChannel);
    }
}