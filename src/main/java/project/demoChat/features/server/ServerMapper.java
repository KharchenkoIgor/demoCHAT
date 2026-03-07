package project.demoChat.features.server;

import org.springframework.stereotype.Component;
import project.demoChat.domain.Server;

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