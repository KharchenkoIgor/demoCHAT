package project.demoChat.config;

import org.springframework.stereotype.Component;

import project.demoChat.domain.Member;
import project.demoChat.domain.Message;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.features.server.repository.MemberRepository;

@Component
public class RolePermission {

    private final MemberRepository memberRepository;

    public RolePermission(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void checkRole(Long serverId, String username, MemberRole requiredRole) {
        Member member = memberRepository.findByServerIdAndUserUsername(serverId, username)
                .orElseThrow(() -> new RuntimeException("このサーバーのメンバーとして参加しているわけではありません"));

        MemberRole userRole = member.getRole();

        if (requiredRole == MemberRole.OWNER) {
            if (userRole != MemberRole.OWNER) throw new RuntimeException("管理者権限が必要です");
        }

        if (requiredRole == MemberRole.ADMIN) {
            if (userRole != MemberRole.ADMIN && userRole != MemberRole.OWNER) {
                throw new RuntimeException("管理者またはアドミニストレータ権限が必要です");
            }
        }
    }

    public void checkMessageEdit(Message message, String username) {

        if (!message.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("自分のメッセージしか編集出来ません");
        }
    }

    public void checkMessageDelete(Message message, String username) {

        if (message.getAuthor().getUsername().equals(username)) return;

        checkRole(message.getChannel().getServer().getId(), username, MemberRole.ADMIN);
    }
}
