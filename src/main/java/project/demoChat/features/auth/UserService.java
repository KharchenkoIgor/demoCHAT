package project.demoChat.features.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import project.demoChat.domain.User;
import project.demoChat.exception.AppException;
import project.demoChat.exception.ErrorCode;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User registerUser(RegistrationDTO request) {

        if (request.getUsername().length() < 3 || request.getUsername().length() > 15) {
            throw new AppException(
                    ErrorCode.VALIDATION_ERROR,
                    "username",
                    "3から15までの字が入力しなければならない！"
            );
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new AppException(
                    ErrorCode.VALIDATION_ERROR,
                    "email",
                    "メールアドレスを入力して下さい"
            );
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(
                    ErrorCode.VALIDATION_ERROR,
                    "email",
                    "このメールアドレスは既に登録されています"
            );
        }

        if (request.getPassword().length() <6) {
            throw new AppException(
                    ErrorCode.VALIDATION_ERROR,
                    "password",
                    "パスワードが短すぎます (最小6字)"
            );
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(
                    ErrorCode.VALIDATION_ERROR,
                    "confirmPassword",
                    "パスワードが一致しません"
            );
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(
                    ErrorCode.VALIDATION_ERROR,
                    "username",
                    "入力したユーザー名はもう存在していますよ!"
            );
        }

        User newUser = new User(
                null,
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                "default_avatar.png"
        );

        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}