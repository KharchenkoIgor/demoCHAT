package project.demoChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.demoChat.model.Server;
import project.demoChat.model.Member;
import project.demoChat.model.enums.MemberRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByServerId(Long serverId);

    Optional<Member> findByServerIdAndUserUsername(Long serverId, String username);

    boolean existsByServerIdAndUserUsername(Long serverId, String username);
}
