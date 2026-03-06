package project.demoChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import project.demoChat.model.User;
import project.demoChat.model.Server;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    List<Server> findByMembersUserUsername(String username);

    List<Server> findByNameContaining(String name);
}
