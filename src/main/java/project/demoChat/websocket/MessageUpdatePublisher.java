package project.demoChat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import project.demoChat.model.Member;
import project.demoChat.model.Message;
import project.demoChat.DTO.MessageDTO;
import project.demoChat.repository.MemberRepository;
import project.demoChat.mapper.MessageMapper;

import java.util.List;

@RequiredArgsConstructor
@Component
public class MessageUpdatePublisher {

    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;

    public void broadcastMessageUpdated(Message message) {

        MessageDTO dto = messageMapper.toDTO(message);

        dto.setType("UPDATE_MESSAGE");

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