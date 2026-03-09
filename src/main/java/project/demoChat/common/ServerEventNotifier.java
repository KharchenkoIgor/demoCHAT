package project.demoChat.common;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import project.demoChat.domain.Member;
import project.demoChat.features.server.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class ServerEventNotifier {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;

    public void broadcastToEntriesServer(Long serverId, Object payload) {
        List<Member> members = memberRepository.findByServerId(serverId);
        for (Member m : members) {
            messagingTemplate.convertAndSendToUser(
                    m.getUser().getUsername(),
                    "/queue/events",
                    payload
            );
        }
    }
}