package project.demoChat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import project.demoChat.model.Server;
import project.demoChat.repository.ServerRepository;
import project.demoChat.usecase.CreateServer;
import project.demoChat.usecase.DeleteServer;
import project.demoChat.usecase.UpdateServer;

@RequiredArgsConstructor
@Service
public class ServerService {

    private final ServerRepository serverRepository;
    private final CreateServer createServer;
    private final DeleteServer deleteServer;
    private final UpdateServer updateServer;

    public Server createServer(String serverName, String username) {
        return createServer.execute(serverName, username);
    }

    public void updateServer(Long id, String newName, String username) {
        updateServer.execute(id, newName, username);
    }

    public void deleteServer(Long id, String username) {
        deleteServer.execute(id,username);
    }

    public List<Server> getUserServers(String username) {
        return serverRepository.findByMembersUserUsername(username);
    }
}