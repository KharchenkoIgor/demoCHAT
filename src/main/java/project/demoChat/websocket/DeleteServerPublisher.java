package project.demoChat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import project.demoChat.DTO.ServerDTO;
import project.demoChat.mapper.ServerMapper;
import project.demoChat.model.Member;
import project.demoChat.model.Server;
import project.demoChat.repository.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DeleteServerPublisher {

    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ServerMapper serverMapper;

    public void broadcastServerDelete(Server server) {
        ServerDTO dto = serverMapper.toDTO(server);
        dto.setType("DELETE_SERVER");

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