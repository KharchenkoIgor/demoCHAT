package project.demoChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import project.demoChat.model.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    List<Channel> findByServerId(Long servetId);
}
