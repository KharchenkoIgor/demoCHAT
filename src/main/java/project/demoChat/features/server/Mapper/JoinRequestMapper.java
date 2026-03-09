package project.demoChat.features.server.Mapper;

import org.springframework.stereotype.Component;
import project.demoChat.domain.JoinRequest;
import project.demoChat.features.server.DTO.JoinRequestDTO;

@Component
public class JoinRequestMapper {

    public JoinRequestDTO toDTO(JoinRequest join) {
        JoinRequestDTO dto = new JoinRequestDTO();
        dto.setId(join.getId());
        dto.setServerId(join.getServer().getId());
        dto.setUsername(join.getUser().getUsername());
        dto.setStatus(join.getStatus().name());

        return dto;
    }
}
