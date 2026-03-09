package project.demoChat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.demoChat.domain.Member;
import project.demoChat.domain.Message;
import project.demoChat.domain.enums.MemberRole;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;
import project.demoChat.features.server.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class ChatSecurity {

    private final MemberRepository memberRepository;

    public void checkMembership(Long serverId, String username) {
        if (!memberRepository.existsByServerIdAndUserUsername(serverId, username)) {
            throw new AppException(ErrorCode.FORBIDDEN, "アクセスが禁止されています: メンバーではありません");
        }
    }

    public void checkRole(Long serverId, String username, MemberRole requiredRole) {
        Member member = memberRepository.findByServerIdAndUserUsername(serverId, username)
                .orElseThrow(() -> new AppException(ErrorCode.FORBIDDEN, "このサーバーのメンバーではありません"));

        MemberRole userRole = member.getRole();

        if (requiredRole == MemberRole.OWNER && userRole != MemberRole.OWNER) {
            throw new AppException(ErrorCode.FORBIDDEN, "管理者 (OWNER) 権限が必要です");
        }

        if (requiredRole == MemberRole.ADMIN && userRole != MemberRole.ADMIN && userRole != MemberRole.OWNER) {
            throw new AppException(ErrorCode.FORBIDDEN, "管理者 (OWNER/ADMIN) 権限が必要です");
        }
    }

    public void checkMessageEdit(Message message, String username) {
        if (!message.getAuthor().getUsername().equals(username)) {
            throw new AppException(ErrorCode.FORBIDDEN, "自分のメッセージしか編集できません");
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