package project.demoChat.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.model.Server;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.repository.ServerRepository;
import project.demoChat.config.RolePermission;
import project.demoChat.websocket.DeleteServerPublisher;

@RequiredArgsConstructor
@Service
public class DeleteServer {

    private final ServerRepository serverRepository;
    private final RolePermission rolePermission;
    private final DeleteServerPublisher deleteServerPublisher;

    @Transactional
    public void execute(Long serverId, String username) {
        rolePermission.checkRole(serverId, username, MemberRole.OWNER);
        Server server = serverRepository.findById(serverId).orElseThrow();

        deleteServerPublisher.broadcastServerDelete(server);

        // 2. Удаляем
        serverRepository.delete(server);
    }
}