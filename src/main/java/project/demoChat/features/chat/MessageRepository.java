package project.demoChat.features.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import project.demoChat.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChannelIdOrderByCreatedAt(Long channelId);
}
