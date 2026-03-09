package project.demoChat.features.server.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.demoChat.common.ServerEventNotifier;
import project.demoChat.domain.Server;
import project.demoChat.features.server.DTO.ServerDTO;
import project.demoChat.features.server.DTO.ServerMapper;

@Component
@RequiredArgsConstructor
public class ServerEventPublisher {
    private final ServerEventNotifier notifier;
    private final ServerMapper mapper;

    public void publishUpdate(Server server) {
        ServerDTO dto = mapper.toDTO(server);
        dto.setType("UPDATE_SERVER");
        notifier.broadcastToEntriesServer(server.getId(), dto);
    }

    public void publishDelete(Server server) {
        ServerDTO dto = mapper.toDTO(server);
        dto.setType("DELETE_SERVER");
        notifier.broadcastToEntriesServer(server.getId(), dto);
    }
}