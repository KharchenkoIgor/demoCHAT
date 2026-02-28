package project.demoChat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import project.demoChat.DTO.ServerDTO;
import project.demoChat.mapper.ServerMapper;
import project.demoChat.model.Member;
import project.demoChat.model.Server;
import project.demoChat.repository.MemberRepository;

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