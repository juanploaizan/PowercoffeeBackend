package com.powercoffee.powercoffeeapirest.utils.services;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    JavaMailSender javaMailSender;

    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String to, String subject, String text) {
        MimeMessage mensaje = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje);
        try {
            helper.setSubject(subject);
            helper.setText(text, false);
            helper.setTo(to);
            helper.setFrom("servicioalcliente.powercoffee@gmail.com");
            javaMailSender.send(mensaje);
        } catch (Exception e) {
            logger.error("Error al enviar el correo: {}", e.getMessage());
        }
    }

}
