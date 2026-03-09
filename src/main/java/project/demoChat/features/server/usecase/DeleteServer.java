package project.demoChat.features.server.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.demoChat.config.ChatSecurity;
import project.demoChat.domain.Server;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.server.repository.ServerRepository;

import project.demoChat.features.server.websocket.ServerEventPublisher;

@RequiredArgsConstructor
@Service
public class DeleteServer {

    private final ServerRepository serverRepository;
    private final ChatSecurity chatSecurity;
    private final ServerEventPublisher serverPublisher;

    @Transactional
    public void execute(Long serverId, String username) {
        chatSecurity.checkRole(serverId, username, MemberRole.OWNER);

        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "サーバーが見つかりません"
                ));

        serverPublisher.publishDelete(server);

        serverRepository.delete(server);
    }
}