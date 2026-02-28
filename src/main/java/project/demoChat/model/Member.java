package project.demoChat.model;

import jakarta.persistence.*;

import project.demoChat.model.enums.MemberRole;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public Member() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Server getServer() {
        return server;
    }
    public void setServer(Server server) {
        this.server = server;
    }

    public MemberRole getRole() {
        return role;
    }
    public void setRole(MemberRole role) {
        this.role = role;
    }
}
