package project.demoChat.DTO;

import project.demoChat.model.enums.SocketType;

public class JoinRequestDTO {
    private Long id;
    private Long serverId;
    private String serverName;
    private String username;
    private String status;
    private SocketType type;

    public JoinRequestDTO() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getServerId() {
        return serverId;
    }
    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(SocketType type) {
        this.type = type;
    }
    public SocketType getType() {
        return type;
    }
}
