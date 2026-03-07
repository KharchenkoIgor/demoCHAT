package project.demoChat.features.chat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import project.demoChat.features.chat.MessageDTO;
import project.demoChat.features.chat.MessageMapper;
import project.demoChat.domain.Member;
import project.demoChat.domain.Message;
import project.demoChat.features.server.repository.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DeleteMessagePublisher {

    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;

    public void broadcastMessageDelete(Message message) {
        MessageDTO dto = messageMapper.toDTO(message);
        dto.setType("DELETE_MESSAGE");

        List<Member> members = memberRepository.findByServerId(message.getChannel().getServer().getId());

        for (Member m : members) {
            messagingTemplate.convertAndSendToUser(
                    m.getUser().getUsername(),
                    "/queue/events",
                    dto
            );
        }
    }
}