package project.demoChat.features.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import project.demoChat.domain.JoinRequest;
import project.demoChat.domain.Server;
import project.demoChat.domain.User;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findByServerIdAndStatus(Long serverId, project.demoChat.domain.enums.JoinRequestStatus status);

    Optional<JoinRequest> findByServerAndUser(Server server, User user);
}
