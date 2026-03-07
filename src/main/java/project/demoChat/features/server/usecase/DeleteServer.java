package project.demoChat.features.server.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.domain.Server;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.server.repository.ServerRepository;
import project.demoChat.config.RolePermission;
import project.demoChat.features.server.websocket.DeleteServerPublisher;

@RequiredArgsConstructor
@Service
public class DeleteServer {

    private final ServerRepository serverRepository;
    private final RolePermission rolePermission;
    private final DeleteServerPublisher deleteServerPublisher;

    @Transactional
    public void execute(Long serverId, String username) {
        rolePermission.checkRole(serverId, username, MemberRole.OWNER);
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "サーバーが見つかりません"
                ));

        deleteServerPublisher.broadcastServerDelete(server);

        serverRepository.delete(server);
    }
}