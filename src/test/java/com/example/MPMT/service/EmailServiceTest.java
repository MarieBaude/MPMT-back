package com.example.MPMT.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender; 

    @InjectMocks
    private EmailService emailService; 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testSendEmailSuccess() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Email Body";

        emailService.sendEmail(to, subject, text);

        // Vérifie que mailSender.send() est appelé une fois avec un SimpleMailMessage
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmailFailure() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Email Body";

        // Simuler une exception lors de l'envoi de l'e-mail
        doThrow(new RuntimeException("Failed to send email")).when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendEmail(to, subject, text);

        // Vérifie que mailSender.send() est appelé une fois malgré l'erreur
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmailLogging() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Email Body";

        emailService.sendEmail(to, subject, text);
    }
}