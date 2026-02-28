package project.demoChat.model;

import jakarta.persistence.*;

import project.demoChat.model.enums.ChannelType;

import java.util.List;

@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public Channel() {}

    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ChannelType getType() {
        return type;
    }
    public void setType(ChannelType type) {
        this.type = type;
    }

    public Server getServer() {
        return server;
    }
    public void setServer(Server server) {
        this.server = server;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    public List<Message> getMessages() {
        return messages;
    }
}
