package com.studleague.studleague.services;

import com.studleague.studleague.configs.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PasswordResetListener {
    private final JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.RESET_PASSWORD_QUEUE)
    public void handlePasswordResetRequest(Map<String, Object> message) {
        System.out.println("Received message: " + message);
        String email = (String) message.get("email");
        String token = (String) message.get("token");

        String resetPasswordUrl = "http://localhost:8080/reset-password";
        String resetLink = String.format("%s?token=%s", resetPasswordUrl, token);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("ds.katrin@mail.ru");

        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click the following link: " + resetLink);

        mailSender.send(mailMessage);
    }
}
