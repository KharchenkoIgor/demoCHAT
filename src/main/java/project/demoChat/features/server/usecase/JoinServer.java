package project.demoChat.features.server.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import project.demoChat.domain.User;
import project.demoChat.domain.Server;
import project.demoChat.domain.Member;
import project.demoChat.domain.JoinRequest;
import project.demoChat.domain.enums.JoinRequestStatus;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.domain.enums.SocketType;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.auth.UserRepository;
import project.demoChat.features.server.repository.ServerRepository;
import project.demoChat.features.server.repository.MemberRepository;
import project.demoChat.features.server.repository.JoinRequestRepository;
import project.demoChat.features.server.DTO.JoinRequestDTO;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JoinServer {
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final MemberRepository memberRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public String execute(Long serverId,String username) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "サーバーが見つかりません"
                ));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "ユーザーが見つかりません"
                ));

        if (memberRepository.existsByServerIdAndUserId(serverId, user.getId())) {
            return "ALREADY_MEMBER";
        }

        if (server.isPublicStatus()) {
            Member newMember = new Member();
            newMember.setUser(user);
            newMember.setServer(server);
            newMember.setRole(MemberRole.USER);

            memberRepository.save(newMember);

            return "JOINED";
        } else {
            Optional<JoinRequest> existingRequest = joinRequestRepository.findByServerAndUser(server, user);

            if (existingRequest.isPresent()) {
                JoinRequest request = existingRequest.get();

                if (request.getStatus() == JoinRequestStatus.PENDING) {
                    return "PENDING_EXISTS";
                }

                if (request.getStatus() == JoinRequestStatus.REJECTED) {
                    request.setStatus(JoinRequestStatus.PENDING);

                    joinRequestRepository.save(request);

                    sendSocketNotification(server, user, request.getId());

                    return "PENDING";
                }
            }

            JoinRequest newRequest = new JoinRequest();
            newRequest.setServer(server);
            newRequest.setUser(user);
            newRequest.setStatus(JoinRequestStatus.PENDING);

            JoinRequest savedRequest = joinRequestRepository.save(newRequest);

            sendSocketNotification(server, user, savedRequest.getId());

            return "PENDING";
        }
    }

    private void sendSocketNotification(Server server, User user, Long requestId) {
        JoinRequestDTO dto = new JoinRequestDTO();
        dto.setId(requestId);
        dto.setServerId(server.getId());
        dto.setUsername(user.getUsername());
        dto.setStatus("PENDING");
        dto.setType(SocketType.NEW_JOIN_REQUEST);

        messagingTemplate.convertAndSend("/topic/server/" + server.getId() + "/requests", dto);
    }
}
