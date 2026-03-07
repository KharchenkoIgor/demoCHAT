package project.demoChat.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private String senderName;
    private Long channelId;
    private Long serverId;
    private String type;
    private String createdAt;
}
