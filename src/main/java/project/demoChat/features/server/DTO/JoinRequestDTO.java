package project.demoChat.features.server.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import project.demoChat.domain.enums.SocketType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDTO {
    private Long id;
    private Long serverId;
    private String serverName;
    private String username;
    private String status;
    private SocketType type;
}
