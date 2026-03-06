package project.demoChat.mapper;

import org.springframework.stereotype.Component;
import project.demoChat.DTO.JoinRequestDTO;
import project.demoChat.model.JoinRequest;

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
