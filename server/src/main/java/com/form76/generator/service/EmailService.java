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

  @Value("${spring.mail.username}")
  private String sender;

  // Method 1
  // To send a simple email
  public void sendSimpleMail(EmailRequest details)
  {

    // Try block to check for exceptions
    try {

      // Creating a simple mail message
      SimpleMailMessage mailMessage
          = new SimpleMailMessage();

      // Setting up necessary details
      mailMessage.setFrom(sender);
      mailMessage.setTo(details.getRecipient());
      mailMessage.setText(details.getMsgBody());
      mailMessage.setSubject(details.getSubject());

      // Sending the mail
      //javaMailSender.send(mailMessage);
      logger.info("Mail sent successfully.");
    } catch (Exception e) {
      logger.error("Error while sending Mail:", e);
      throw  e;
    }
  }

  // Method 2
  // To send an email with attachment
  public void sendMailWithAttachment(EmailRequest details)
  {
    // Creating a mime message
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper;

    try {
      // Setting multipart as true for attachments to
      // be send
      mimeMessageHelper
          = new MimeMessageHelper(mimeMessage, true);
      mimeMessageHelper.setFrom(sender);
      mimeMessageHelper.setTo(details.getRecipient());
      mimeMessageHelper.setText(details.getMsgBody());
      mimeMessageHelper.setSubject(
          details.getSubject());

      // Adding the attachment
      FileSystemResource file
          = new FileSystemResource(
          new File(details.getAttachment()));

      mimeMessageHelper.addAttachment(
          file.getFilename(), file);

      // Sending the mail
      javaMailSender.send(mimeMessage);
      logger.info("Mail sent successfully.");
    } catch (MessagingException e) {
      logger.error("Error while sending mail:", e);
    }
  }

}
