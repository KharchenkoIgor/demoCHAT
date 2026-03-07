package project.demoChat.features.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
