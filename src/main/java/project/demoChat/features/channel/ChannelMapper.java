package project.demoChat.features.channel;

import org.springframework.stereotype.Component;
import project.demoChat.domain.Channel;

@Component
public class ChannelMapper {

    public ChannelDTO toDTO(Channel channel) {

        ChannelDTO dto = new ChannelDTO();

        dto.setId(channel.getId());
        dto.setName(channel.getName());
        dto.setServerId(channel.getServer().getId());

        dto.setType("CHANNEL");

        return dto;
    }
}