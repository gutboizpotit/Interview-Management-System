package btl.g4.mock.notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
public class EmailServiceImpl implements MessengerService {

    @Value("${spring.mail.from-address:noreply@amatrium.com}")
    private String fromAddress;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void sendMessageInAsync(EmailMessage email) {
        Map<String, String> args = email.getArguments();
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            helper.setFrom(fromAddress);
            helper.setSubject(email.getSubject());
            helper.setTo(email.getReceiver());

            Context context = new Context();
            for (Map.Entry<String, String> varEntry : args.entrySet()) {
                context.setVariable(varEntry.getKey(), varEntry.getValue());
            }

            String html = templateEngine.process(email.getTemplate(), context);
            helper.setText(html, true);

            javaMailSender.send(mimeMessage);
            log.info("Send mail reset password successfully");
        } catch (MessagingException messagingException) {
            log.error("Failed to send the email", messagingException);
        }
    }
}
