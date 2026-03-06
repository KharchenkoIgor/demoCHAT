package project.demoChat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

import project.demoChat.config.ChatSecurity;
import project.demoChat.model.Member;
import project.demoChat.model.JoinRequest;
import project.demoChat.model.enums.JoinRequestStatus;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.model.enums.SocketType;
import project.demoChat.repository.MemberRepository;
import project.demoChat.repository.JoinRequestRepository;

@RequiredArgsConstructor
@Component
public class AcceptedJoinRequest {
    private final ChatSecurity chatSecurity;
    private final MemberRepository memberRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void execute(Long requestId, String username) {
        JoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("申請が見つかりません"));

        if (request.getStatus() != JoinRequestStatus.PENDING) {
            throw new RuntimeException("申請はもう承認されています");
        }

        chatSecurity.checkRole(request.getServer().getId(), username, MemberRole.OWNER);

        request.setStatus(JoinRequestStatus.ACCEPTED);
        joinRequestRepository.save(request);

        if (!memberRepository.existsByServerIdAndUserId(request.getServer().getId(), request.getUser().getId())) {

            Member newMember = new Member();
            newMember.setUser(request.getUser());
            newMember.setServer(request.getServer());
            newMember.setRole(MemberRole.USER);

            memberRepository.save(newMember);
        }


        String targetUsername = request.getUser().getUsername();
        messagingTemplate.convertAndSendToUser(
                targetUsername,
                "/queue/reply",
                Map.of(
                        "type", SocketType.JOIN_RESPONSE.name(),
                        "responseType", "ACCEPTED",
                        "serverId", request.getServer().getId(),
                        "serverName", request.getServer().getName()
                )
        );
    }
}
