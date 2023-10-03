package com.powercoffee.utils.services;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        MimeMessage mensaje = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje);
        try {
            helper.setSubject(subject);
            helper.setText(text, false);
            helper.setTo(to);
            helper.setFrom("clientes.unicine@gmail.com");
            javaMailSender.send(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
