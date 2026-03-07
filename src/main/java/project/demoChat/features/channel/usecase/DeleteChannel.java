package project.demoChat.features.channel.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.domain.Channel;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.features.channel.ChannelRepository;
import project.demoChat.config.RolePermission;
import project.demoChat.features.channel.websocket.DeleteChannelPublisher;

@RequiredArgsConstructor
@Service
public class DeleteChannel {

    private final ChannelRepository channelRepository;
    private final RolePermission rolePermission;
    private final DeleteChannelPublisher deleteChannelPublisher;

    @Transactional
    public void execute(Long channelId, String username) {
        Channel channel = channelRepository.findById(channelId).orElseThrow();
        rolePermission.checkRole(channel.getServer().getId(), username, MemberRole.ADMIN);

        deleteChannelPublisher.broadcastChannelDelete(channel);

        channelRepository.delete(channel);
    }
}