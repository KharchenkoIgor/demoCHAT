package project.demoChat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import project.demoChat.config.ChatSecurity;
import project.demoChat.model.JoinRequest;
import project.demoChat.model.enums.JoinRequestStatus;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.model.enums.SocketType;
import project.demoChat.repository.JoinRequestRepository;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class RejectJoinRequest {
    private final JoinRequestRepository joinRequestRepository;
    private final ChatSecurity chatSecurity;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void execute(Long requestId, String username) {
        JoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("申請が見つかりません"));

        chatSecurity.checkRole(request.getServer().getId(), username, MemberRole.OWNER);

        request.setStatus(JoinRequestStatus.REJECTED);
        joinRequestRepository.save(request);

        String targetUsername = request.getUser().getUsername();
        messagingTemplate.convertAndSendToUser(
                targetUsername,
                "/queue/reply",
                Map.of(
                        "type", SocketType.JOIN_RESPONSE.name(),
                        "responseType", "REJECTED",
                        "serverName", request.getServer().getName()
                )
        );
    }
}
