package project.demoChat.features.server.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.demoChat.config.ChatSecurity;
import project.demoChat.domain.Server;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.server.repository.ServerRepository;
import project.demoChat.features.server.websocket.ServerEventPublisher;

@RequiredArgsConstructor
@Component
public class UpdateServer {

    private final ServerRepository serverRepository;
    private final ChatSecurity chatSecurity;
    private final ServerEventPublisher serverPublisher;

    @Transactional
    public void execute(Long serverId, String newName, String username) {

        chatSecurity.checkRole(serverId, username, MemberRole.OWNER);

        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "サーバーが見つかりません"
                ));

        server.setName(newName);
        Server savedServer = serverRepository.save(server);

        serverPublisher.publishUpdate(savedServer);
    }
}