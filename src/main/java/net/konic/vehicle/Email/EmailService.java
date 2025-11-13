package net.konic.vehicle.Email;

import net.konic.vehicle.dto.ReminderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(ReminderDTO dto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(dto.getToEmail());
            message.setSubject(dto.getSubject());
            message.setText(dto.getMessage());
            mailSender.send(message);

            logger.info(" Email sent to: " + dto.getToEmail() + " | Subject: " + dto.getSubject());
        } catch (Exception e) {
            logger.warning(" Failed to send email to " + dto.getToEmail() + ": " + e.getMessage());
        }
    }
}