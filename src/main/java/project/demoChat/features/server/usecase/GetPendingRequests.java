package project.demoChat.features.server.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import project.demoChat.config.ChatSecurity;
import project.demoChat.domain.JoinRequest;
import project.demoChat.domain.enums.JoinRequestStatus;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.features.server.repository.JoinRequestRepository;

@RequiredArgsConstructor
@Component
public class GetPendingRequests {
    private final JoinRequestRepository joinRequestRepository;
    private final ChatSecurity chatSecurity;

    public List<JoinRequest> execute(Long serverId, String username) {

        chatSecurity.checkRole(serverId, username, MemberRole.OWNER);

        return joinRequestRepository.findByServerIdAndStatus(serverId, JoinRequestStatus.PENDING);
    }
}