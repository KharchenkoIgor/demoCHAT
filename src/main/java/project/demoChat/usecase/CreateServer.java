package project.demoChat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import project.demoChat.model.*;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.repository.UserRepository;
import project.demoChat.repository.ServerRepository;
import project.demoChat.repository.ChannelRepository;
import project.demoChat.repository.MemberRepository;

@RequiredArgsConstructor
@Component
public class CreateServer {

    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Server execute(String serverName, boolean isPublicStatus, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + username));

        Server newServer = new Server();
        newServer.setName(serverName);
        newServer.setOwner(owner);
        newServer.setPublicStatus(isPublicStatus);
        Server savedServer = serverRepository.save(newServer);

        Member newMember = new Member();
        newMember.setUser(owner);
        newMember.setServer(savedServer);
        newMember.setRole(MemberRole.OWNER);
        memberRepository.save(newMember);

        Channel defaultChannel = new Channel();
        defaultChannel.setName("general");
        defaultChannel.setServer(savedServer);
        channelRepository.save(defaultChannel);

        return savedServer;
    }
}