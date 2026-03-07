package project.demoChat.features.channel.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.demoChat.domain.Server;
import project.demoChat.domain.Channel;
import project.demoChat.features.channel.ChannelDTO;
import project.demoChat.features.channel.ChannelRepository;
import project.demoChat.features.server.repository.ServerRepository;

@RequiredArgsConstructor
@Service
public class CreateChannel {

    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;

    @Transactional
    public Channel execute(ChannelDTO channelDTO) {
        Server server = serverRepository.findById(channelDTO.getServerId())
                .orElseThrow(() -> new RuntimeException("サーバーが見つかりません"));

        Channel newChannel = new Channel();
        newChannel.setName(channelDTO.getName());
        newChannel.setServer(server);

        return channelRepository.save(newChannel);
    }
}