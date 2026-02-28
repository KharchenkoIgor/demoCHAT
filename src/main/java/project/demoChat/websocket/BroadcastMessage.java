package project.demoChat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import project.demoChat.DTO.MessageDTO;
import project.demoChat.model.Member;
import project.demoChat.repository.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BroadcastMessage {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;

    public void execute(MessageDTO messageDTO) {
        List<Member> members = memberRepository.findByServerId(messageDTO.getServerId());

        for (Member member : members) {
            messagingTemplate.convertAndSendToUser(
                    member.getUser().getUsername(),
                    "/queue/events",
                    messageDTO
            );
        }
    }
}