package project.demoChat.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.demoChat.model.Server;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.repository.ServerRepository;
import project.demoChat.config.RolePermission;
import project.demoChat.websocket.ServerUpdatePublisher;

@RequiredArgsConstructor
@Component
public class UpdateServer {

    private final ServerRepository serverRepository;
    private final RolePermission rolePermission;
    private final ServerUpdatePublisher serverUpdatePublisher;

    @Transactional
    public void execute(Long serverId, String newName, String username) {

        rolePermission.checkRole(serverId, username, MemberRole.OWNER);

        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("サーバーが見つかりません"));

        server.setName(newName);
        Server savedServer = serverRepository.save(server);

        serverUpdatePublisher.broadcastServerUpdated(savedServer);
    }
}