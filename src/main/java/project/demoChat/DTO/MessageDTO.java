package project.demoChat.DTO;

import java.time.LocalDateTime;

import project.demoChat.model.enums.SocketType;

public class MessageDTO {
    private Long id;
    private String content;
    private String senderName;
    private Long channelId;
    private Long serverId;
    private String type;
    private String createdAt;

    public MessageDTO() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public Long getChannelId() { return channelId; }
    public void setChannelId(Long channelId) { this.channelId = channelId; }

    public Long getServerId() { return serverId; }
    public void setServerId(Long serverId) { this.serverId = serverId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getCreatedAt() {
        return createdAt;
    }
}
