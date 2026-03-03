package project.demoChat.config;

import org.springframework.stereotype.Component;
import project.demoChat.repository.MemberRepository;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.model.Member;

@Component
public class ChatSecurity {

    private final MemberRepository memberRepository;

    public ChatSecurity(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void checkMembership(Long serverId, String username) {
        if (!memberRepository.existsByServerIdAndUserUsername(serverId, username)) {
            throw new RuntimeException("アクセスが禁止されている原因は: メンバではありません");
        }
    }

    public String getUserRole(Long serverId, String username) {
        return memberRepository.findByServerIdAndUserUsername(serverId, username)
                .map(m -> m.getRole().name())
                .orElse("NONE");
    }
}