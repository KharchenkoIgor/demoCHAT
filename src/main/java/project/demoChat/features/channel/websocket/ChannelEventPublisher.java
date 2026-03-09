package project.demoChat.features.channel.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.demoChat.common.ServerEventNotifier;
import project.demoChat.domain.Channel;
import project.demoChat.features.channel.ChannelDTO;
import project.demoChat.features.channel.ChannelMapper;

@RequiredArgsConstructor
@Component
public class ChannelEventPublisher {
    private final ServerEventNotifier notifier;
    private final ChannelMapper mapper;

    public void publishUpdate(Channel channel) {
        ChannelDTO dto = mapper.toDTO(channel);
        dto.setType("UPDATE_CHANNEL");
        notifier.broadcastToEntriesServer(channel.getServer().getId(), dto);
    }

    public void publishDelete(Channel channel) {
        ChannelDTO dto = mapper.toDTO(channel);
        dto.setType("DELETE_CHANNEL");
        notifier.broadcastToEntriesServer(channel.getServer().getId(), dto);
    }
}