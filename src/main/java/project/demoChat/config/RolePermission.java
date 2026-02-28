package project.demoChat.config;

import org.springframework.stereotype.Component;

import project.demoChat.model.Member;
import project.demoChat.model.Message;
import project.demoChat.model.enums.MemberRole;
import project.demoChat.repository.MemberRepository;

@Component
public class RolePermission {

    private final MemberRepository memberRepository;

    public RolePermission(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // Универсальная проверка: есть ли у пользователя нужная роль или выше?
    public void checkRole(Long serverId, String username, MemberRole requiredRole) {
        Member member = memberRepository.findByServerIdAndUserUsername(serverId, username)
                .orElseThrow(() -> new RuntimeException("Вы не являетесь участником этого сервера"));

        MemberRole userRole = member.getRole();

        // Логика иерархии:
        // Если нам нужен OWNER, то пускаем только OWNER
        if (requiredRole == MemberRole.OWNER) {
            if (userRole != MemberRole.OWNER) throw new RuntimeException("Нужны права владельца");
        }

        // Если нам нужен ADMIN, то пускаем и ADMIN, и OWNER
        if (requiredRole == MemberRole.ADMIN) {
            if (userRole != MemberRole.ADMIN && userRole != MemberRole.OWNER) {
                throw new RuntimeException("Нужны права администратора или владельца");
            }
        }

        // Обычному USER удалять ничего нельзя, поэтому для него проверок не делаем
    }

    // Специальная проверка для сообщений (Автор или Админ+)
    public void checkMessageDelete(Message message, String username) {
        // 1. Если автор - ок
        if (message.getAuthor().getUsername().equals(username)) return;

        // 2. Если не автор - проверяем, есть ли у него роль ADMIN или OWNER на этом сервере
        checkRole(message.getChannel().getServer().getId(), username, MemberRole.ADMIN);
    }
}
