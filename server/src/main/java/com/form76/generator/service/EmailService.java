package com.form76.generator.service;

import com.form76.generator.service.model.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {
  Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Autowired
  private JavaMailSender javaMailSender;

  @Value("${form76-generator.emails.enabled}")
  private boolean emailsEnabled = false;

  @Value("${spring.mail.username}")
  private String sender;


  public void sendSimpleMail(EmailRequest details)
  {

    try {

      SimpleMailMessage mailMessage
          = new SimpleMailMessage();

      mailMessage.setFrom(sender);
      mailMessage.setTo(details.getRecipient());
      mailMessage.setText(details.getMsgBody());
      mailMessage.setSubject(details.getSubject());

      if (emailsEnabled) {
        javaMailSender.send(mailMessage);
        logger.info("Email sent successfully to " + details.getRecipient());
      } else {
        logger.info("Actual sending of emails is disabled (application.properties -> form76-generator.emails.enabled=false). " +
            "So sending email to " + details.getRecipient() + " is skipped");
      }
    } catch (Exception e) {
      logger.error("Error while sending Mail:", e);
      throw  e;
    }
  }

  public void sendMailWithAttachment(EmailRequest details)
  {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper;

    try {

      mimeMessageHelper
          = new MimeMessageHelper(mimeMessage, true);
      mimeMessageHelper.setFrom(sender);
      mimeMessageHelper.setTo(details.getRecipient());
      mimeMessageHelper.setText(details.getMsgBody());
      mimeMessageHelper.setSubject(
          details.getSubject());

      FileSystemResource file
          = new FileSystemResource(
          new File(details.getAttachment()));

      mimeMessageHelper.addAttachment(file.getFilename(), file);

      if (emailsEnabled) {
        javaMailSender.send(mimeMessage);
        logger.info("Email sent successfully to " + details.getRecipient());
      } else {
        logger.info("Actual sending of emails is disabled (application.properties -> form76-generator.emails.enabled=false). " +
            "So sending email to " + details.getRecipient() + " is skipped");
      }
    } catch (MessagingException e) {
      logger.error("Error while sending mail:", e);
    }
  }

}
