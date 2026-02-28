package project.demoChat.DTO;

public class ChannelDTO {
    private Long id;
    private String name;
    private Long serverId;
    private String type;

    public ChannelDTO() {}

    public ChannelDTO(Long id, String name, Long serverId) {
        this.id = id;
        this.name = name;
        this.serverId = serverId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getServerId() { return serverId; }
    public void setServerId(Long serverId) { this.serverId = serverId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}