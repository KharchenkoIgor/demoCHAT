package project.demoChat.features.channel.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import project.demoChat.features.channel.ChannelDTO;
import project.demoChat.features.channel.ChannelMapper;
import project.demoChat.domain.Channel;
import project.demoChat.domain.Member;
import project.demoChat.features.server.repository.MemberRepository;

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