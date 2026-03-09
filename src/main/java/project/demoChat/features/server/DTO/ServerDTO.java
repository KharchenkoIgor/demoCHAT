package project.demoChat.features.server.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerDTO {
    private Long id;
    private String name;
    private boolean publicStatus;
    private String type;
}