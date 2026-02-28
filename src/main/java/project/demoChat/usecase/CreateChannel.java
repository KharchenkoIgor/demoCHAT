package project.demoChat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.demoChat.model.Server;
import project.demoChat.model.Channel;
import project.demoChat.DTO.ChannelDTO;
import project.demoChat.repository.ChannelRepository;
import project.demoChat.repository.ServerRepository;

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