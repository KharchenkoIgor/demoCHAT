package project.demoChat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import project.demoChat.DTO.ChannelDTO;
import project.demoChat.mapper.ChannelMapper;
import project.demoChat.model.Channel;
import project.demoChat.model.Member;
import project.demoChat.repository.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ChannelUpdatePublisher {

    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChannelMapper channelMapper;

    public void broadcastChannelUpdated(Channel channel) {
        ChannelDTO dto = channelMapper.toDTO(channel);
        dto.setType("UPDATE_CHANNEL");

        List<Member> members = memberRepository.findByServerId(channel.getServer().getId());

        for (Member m : members) {
            messagingTemplate.convertAndSendToUser(
                    m.getUser().getUsername(),
                    "/queue/events",
                    dto
            );
        }
    }
}