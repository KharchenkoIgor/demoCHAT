package project.demoChat.features.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.demoChat.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByServerId(Long serverId);

    Optional<Member> findByServerIdAndUserUsername(Long serverId, String username);

    boolean existsByServerIdAndUserUsername(Long serverId, String username);

    boolean existsByServerIdAndUserId(Long serverId, Long userId);
}
