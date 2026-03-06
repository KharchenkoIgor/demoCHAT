package project.demoChat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.demoChat.model.Member;
import project.demoChat.model.Message;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class ChatSecurity {

    private final MemberRepository memberRepository;

    public void checkMembership(Long serverId, String username) {
        if (!memberRepository.existsByServerIdAndUserUsername(serverId, username)) {
            throw new RuntimeException("アクセスが禁止されています: メンバーではありません");
        }
    }

    public void checkRole(Long serverId, String username, MemberRole requiredRole) {
        Member member = memberRepository.findByServerIdAndUserUsername(serverId, username)
                .orElseThrow(() -> new RuntimeException("このサーバーのメンバーではありません"));

        MemberRole userRole = member.getRole();

        if (requiredRole == MemberRole.OWNER && userRole != MemberRole.OWNER) {
            throw new RuntimeException("管理者 (OWNER) 権限が必要です");
        }

        if (requiredRole == MemberRole.ADMIN && userRole != MemberRole.ADMIN && userRole != MemberRole.OWNER) {
            throw new RuntimeException("管理者 (OWNER/ADMIN) 権限が必要です");
        }
    }

    public void checkMessageEdit(Message message, String username) {
        if (!message.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("自分のメッセージしか編集できません");
        }
    }

    public void checkMessageDelete(Message message, String username) {
        if (message.getAuthor().getUsername().equals(username)) {
            return;
        }

        checkRole(message.getChannel().getServer().getId(), username, MemberRole.ADMIN);
    }

    public String getUserRole(Long serverId, String username) {
        return memberRepository.findByServerIdAndUserUsername(serverId, username)
                .map(m -> m.getRole().name())
                .orElse("NONE");
    }
}