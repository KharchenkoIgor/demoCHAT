package project.demoChat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import project.demoChat.model.JoinRequest;
import project.demoChat.model.Server;
import project.demoChat.repository.ServerRepository;
import project.demoChat.usecase.CreateServer;
import project.demoChat.usecase.DeleteServer;
import project.demoChat.usecase.UpdateServer;
import project.demoChat.usecase.JoinServer;
import project.demoChat.usecase.GetPendingRequests;
import project.demoChat.usecase.AcceptedJoinRequest;
import project.demoChat.usecase.RejectJoinRequest;

@RequiredArgsConstructor
@Service
public class ServerService {

    private final ServerRepository serverRepository;
    private final CreateServer createServer;
    private final DeleteServer deleteServer;
    private final UpdateServer updateServer;
    private final JoinServer joinServer;
    private final GetPendingRequests getPendingRequests;
    private final AcceptedJoinRequest acceptedJoinRequest;
    private final RejectJoinRequest rejectJoinRequest;

    public Server createServer(String serverName, boolean isPublicStatus, String username) {
        return createServer.execute(serverName, isPublicStatus, username);
    }

    public void updateServer(Long id, String newName, String username) {
        updateServer.execute(id, newName, username);
    }

    public void deleteServer(Long id, String username) {
        deleteServer.execute(id,username);
    }

    public String joinServer(Long serverId, String username) {
        return joinServer.execute(serverId, username);
    }

    public List<JoinRequest> getPendingRequests(Long serverId, String username) {
        return getPendingRequests.execute(serverId, username);
    }

    public void acceptedJoinRequest(Long requestId, String username) {
        acceptedJoinRequest.execute(requestId, username);
    }

    public void rejectJoinRequest(Long requestId, String username) {
        rejectJoinRequest.execute(requestId, username);
    }

    public List<Server> getUserServers(String username) {
        return serverRepository.findByMembersUserUsername(username);
    }
    public List<Server> searchServer(String name) {
        return serverRepository.findByNameContaining(name);
    }
}