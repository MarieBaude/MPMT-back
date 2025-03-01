package com.example.MPMT.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            logger.info("Tentative d'envoi d'e-mail à : {}", to); // Log avant l'envoi
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("E-mail envoyé avec succès à : {}", to); // Log après l'envoi
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'e-mail à : {}", to, e); // Log en cas d'erreur
        }
    }
}