package project.demoChat.mapper;

import org.springframework.stereotype.Component;
import project.demoChat.DTO.ChannelDTO;
import project.demoChat.model.Channel;

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