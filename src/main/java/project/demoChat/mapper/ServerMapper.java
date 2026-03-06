package project.demoChat.mapper;

import org.springframework.stereotype.Component;
import project.demoChat.DTO.ServerDTO;
import project.demoChat.model.Server;

@Component
public class ServerMapper {

    public ServerDTO toDTO(Server server) {

        ServerDTO dto = new ServerDTO();

        dto.setId(server.getId());
        dto.setName(server.getName());
        dto.setPublicStatus(server.isPublicStatus());
        dto.setType("SERVER");

        return dto;
    }
}