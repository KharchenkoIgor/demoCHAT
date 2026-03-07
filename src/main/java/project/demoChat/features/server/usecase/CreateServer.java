package project.demoChat.features.server.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import project.demoChat.domain.*;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.auth.UserRepository;
import project.demoChat.features.server.repository.ServerRepository;
import project.demoChat.features.channel.ChannelRepository;
import project.demoChat.features.server.repository.MemberRepository;

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
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "サーバーが見つかりません" + username
                ));

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