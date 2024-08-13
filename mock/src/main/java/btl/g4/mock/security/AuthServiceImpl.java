package btl.g4.mock.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import btl.g4.mock.dto.BaseMessageDto;
import btl.g4.mock.entity.User;
import btl.g4.mock.notification.EmailMessage;
import btl.g4.mock.notification.MessengerService;
import btl.g4.mock.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.utility.RandomString;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessengerService emailService;

    @Override
    public BaseMessageDto sendResetPasswordEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return BaseMessageDto.builder()
                    .status(0)
                    .message("Invalid email")
                    .build();
        }

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return BaseMessageDto.builder()
                    .status(0)
                    .message("The user do not exist in your organization")
                    .build();
        }

        String randomCode = RandomString.make(12);
        userOpt.get().setPassword(passwordEncoder.encode(randomCode));
        userRepository.save(userOpt.get());

        Map<String, String> emailArgs = new HashMap<>();
        emailArgs.put("ARG_USERNAME", userOpt.get().getUsername());
        emailArgs.put("ARG_PASSWORD", randomCode);

        EmailMessage emailInfo = EmailMessage.builder()
                .template("email_reset_password.html")
                .receiver(userOpt.get().getEmail())
                .subject("Reset password")
                .arguments(emailArgs)
                .build();
        emailService.sendMessageInAsync(emailInfo);

        return null;
    }

    @Override
    public Object resetUserPassword(String randomCode, String email) {

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return BaseMessageDto.builder()
                    .status(0)
                    .message("The user do not exist in your organization")
                    .build();
        }

        if (userOpt.get().getSecret().equals(randomCode)) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(RandomString.make(12)));
            userRepository.save(user);

            user.setSecret(null);
            return true;
        }
        return BaseMessageDto.builder()
                .status(0)
                .message("The code seems to be invalid or expired")
                .build();
    }
}
