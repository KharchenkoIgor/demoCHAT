package project.demoChat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import project.demoChat.config.ChatSecurity;
import project.demoChat.model.JoinRequest;
import project.demoChat.model.enums.JoinRequestStatus;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.repository.JoinRequestRepository;

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