package project.demoChat.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.demoChat.model.Channel;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.repository.ChannelRepository;
import project.demoChat.config.RolePermission;
import project.demoChat.websocket.ChannelUpdatePublisher;

@RequiredArgsConstructor
@Component
public class UpdateChannel {

    private final ChannelRepository channelRepository;
    private final RolePermission rolePermission;
    private final ChannelUpdatePublisher channelUpdatePublisher;

    @Transactional
    public void execute(Long channelId, String newName, String username) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("チャンネルが見つかりません"));

        rolePermission.checkRole(channel.getServer().getId(), username, MemberRole.OWNER);

        channel.setName(newName);
        Channel savedChannel = channelRepository.save(channel);

        channelUpdatePublisher.broadcastChannelUpdated(savedChannel);
    }
}