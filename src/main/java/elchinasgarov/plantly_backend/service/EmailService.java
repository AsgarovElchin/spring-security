package elchinasgarov.plantly_backend.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final SendGrid sendGrid;

    @Value("${sendgrid.sender.email}")
    private String senderEmail;

    public EmailService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendOtpEmail(String toEmail, String otp) {
        Email from = new Email(senderEmail);
        Email to = new Email(toEmail);
        String subject = "Your OTP Code";
        String contentText = "Your OTP code is: " + otp + ". It will expire in 5 minutes.";
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            logger.info("Email sent to {} with status {}", toEmail, response.getStatusCode());
        } catch (IOException ex) {
            logger.error("Failed to send OTP email to {}: {}", toEmail, ex.getMessage(), ex);
        }
    }

    public void sendEmail(String toEmail, String subject, String contentText) {
        Email from = new Email(senderEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            logger.info("Email sent to {} with status {}", toEmail, response.getStatusCode());
        } catch (IOException ex) {
            logger.error("Failed to send email to {}: {}", toEmail, ex.getMessage(), ex);
        }
    }
}