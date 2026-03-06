package project.demoChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import project.demoChat.model.JoinRequest;
import project.demoChat.model.Server;
import project.demoChat.model.User;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findByServerIdAndStatus(Long serverId, project.demoChat.model.enums.JoinRequestStatus status);

    Optional<JoinRequest> findByServerAndUser(Server server, User user);
}
