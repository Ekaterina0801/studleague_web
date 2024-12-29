package com.studleague.studleague.services;

import com.studleague.studleague.configs.RabbitMQConfig;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PasswordResetListener {
    private final JavaMailSender mailSender;
    @Value("${frontend.url}/reset-password")
    private String resetPasswordUrl;
    @RabbitListener(queues = RabbitMQConfig.RESET_PASSWORD_QUEUE)
    @RabbitListener(queues = RabbitMQConfig.RESET_PASSWORD_QUEUE)
    public void handlePasswordResetRequest(Map<String, Object> message) {
        System.out.println("Received message: " + message);
        String email = (String) message.get("email");
        String token = (String) message.get("token");

        String resetLink = String.format("%s?token=%s", resetPasswordUrl, token);

        String htmlContent = """
                <div style="font-family: 'Rubik', sans-serif; text-align: center; padding: 20px; background-color: #f5efed; color: #330036; border-radius: 10px; max-width: 600px; margin: auto; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);">
                    <h1 style="color: #330036;">Восстановление пароля</h1>
                    <p style="font-size: 16px;">Здравствуйте,</p>
                    <p style="font-size: 16px;">Вы отправили запрос на восстановление пароля. Пожалуйста, нажмите на кнопку для восстановления::</p>
                    <a href="%s" style="display: inline-block; padding: 12px 20px; font-size: 16px; color: #fff; background-color: #330036; border: none; border-radius: 5px; text-decoration: none; margin-top: 10px;">Восстановить пароль</a>
                    <p style="font-size: 14px; color: #555; margin-top: 20px;">Если вы не отправляли запрос, игнорируйте это сообщение.</p>
                    <p style="font-size: 14px; color: #555;"><br>Сайт рейтинга "Имя розы"</p>
                </div>
                """.formatted(resetLink);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("ds.katrin@mail.ru");
            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            System.out.println("Password reset email sent successfully to " + email);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error sending password reset email: " + e.getMessage());
        }
    }
}
