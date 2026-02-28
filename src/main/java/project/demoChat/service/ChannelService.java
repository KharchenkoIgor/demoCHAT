package project.demoChat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import project.demoChat.model.Channel;
import project.demoChat.DTO.ChannelDTO;
import project.demoChat.repository.ChannelRepository;
import project.demoChat.usecase.CreateChannel;
import project.demoChat.usecase.DeleteChannel;
import project.demoChat.usecase.UpdateChannel;

@RequiredArgsConstructor
@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final CreateChannel createChannel;
    private final DeleteChannel deleteChannel;
    private final UpdateChannel updateChannel;

    public Channel createChannel(ChannelDTO channelDTO) {
        return createChannel.execute(channelDTO);
    }

    public void updateChannel(Long id, String newName, String username) {
        updateChannel.execute(id, newName, username);
    }

    public void deleteChannel(Long id, String username) {
        deleteChannel.execute(id, username);
    }

    public List<Channel> getChannelsByServer(Long serverId) {
        return channelRepository.findByServerId(serverId);
    }
}