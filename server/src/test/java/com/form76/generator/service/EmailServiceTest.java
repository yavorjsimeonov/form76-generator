package com.form76.generator.service;

import com.form76.generator.service.model.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

  private JavaMailSender javaMailSender;
  private EmailService emailService;

  @BeforeEach
  void setUp() {
    javaMailSender = mock(JavaMailSender.class);
    emailService = new EmailService();
    emailService.javaMailSender = javaMailSender;
    emailService.sender = "test@domain.com";
  }

  @Test
  void testSendSimpleMail_EmailsEnabled() {
    EmailRequest request = new EmailRequest("to@domain.com", "Test body", "Test subject", null);
    emailService.emailsEnabled = true;

    emailService.sendSimpleMail(request);

    ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
    verify(javaMailSender, times(1)).send(captor.capture());
    assertEquals("to@domain.com", captor.getValue().getTo()[0]);
    assertEquals("Test body", captor.getValue().getText());
    assertEquals("Test subject", captor.getValue().getSubject());
  }

  @Test
  void testSendSimpleMail_EmailsDisabled() {
    EmailRequest request = new EmailRequest("to@domain.com", "Test body", "Test subject", null);
    emailService.emailsEnabled = false;

    emailService.sendSimpleMail(request);

    verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
  }

  @Test
  void testSendMailWithAttachment_EmailsEnabled() throws MessagingException {
    EmailRequest request = new EmailRequest(
        "to@domain.com", "Message with attachment", "Attachment Subject", "src/test/resources/testfile.txt"
    );
    emailService.emailsEnabled = true;

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    doNothing().when(javaMailSender).send(mimeMessage);

    File file = new File(request.getAttachment());
    assertTrue(file.exists(), "Attachment file must exist for this test.");

    emailService.sendMailWithAttachment(request);

    verify(javaMailSender).send(any(MimeMessage.class));
  }

  @Test
  void testSendMailWithAttachment_EmailsDisabled() throws MessagingException {
    EmailRequest request = new EmailRequest(
        "to@domain.com", "Message with attachment", "Attachment Subject", "src/test/resources/testfile.txt"
    );
    emailService.emailsEnabled = false;

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

    emailService.sendMailWithAttachment(request);

    verify(javaMailSender, never()).send(any(MimeMessage.class));
  }
}
