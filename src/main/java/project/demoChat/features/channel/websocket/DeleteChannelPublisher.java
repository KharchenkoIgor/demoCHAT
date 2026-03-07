package project.demoChat.features.channel.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import project.demoChat.features.channel.ChannelDTO;
import project.demoChat.features.channel.ChannelMapper;
import project.demoChat.domain.Channel;
import project.demoChat.domain.Member;
import project.demoChat.features.server.repository.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DeleteChannelPublisher {

    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChannelMapper channelMapper;

    public void broadcastChannelDelete(Channel channel) {
        ChannelDTO dto = channelMapper.toDTO(channel);
        dto.setType("DELETE_CHANNEL");

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