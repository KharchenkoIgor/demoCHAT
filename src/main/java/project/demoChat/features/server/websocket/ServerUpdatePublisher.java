package project.demoChat.features.server.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import project.demoChat.features.server.ServerDTO;
import project.demoChat.features.server.ServerMapper;
import project.demoChat.domain.Member;
import project.demoChat.domain.Server;
import project.demoChat.features.server.repository.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ServerUpdatePublisher {

    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ServerMapper serverMapper;

    public void broadcastServerUpdated(Server server) {
        ServerDTO dto = serverMapper.toDTO(server);
        dto.setType("UPDATE_SERVER");

        List<Member> members = memberRepository.findByServerId(server.getId());

        for (Member m : members) {
            messagingTemplate.convertAndSendToUser(
                    m.getUser().getUsername(),
                    "/queue/events",
                    dto
            );
        }
    }
}