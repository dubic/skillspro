package me.skillspro.notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Service
public class ElasticEmailSMTP {
    private final JavaMailSender sender;

    public ElasticEmailSMTP(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendMail() throws UnsupportedEncodingException, MessagingException {
        MimeMessage mime = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true);

        helper.setText("Welcome to skillspro", true);

        helper.setTo("udubic@gmail.com");
        helper.setSubject("Test user registration email");
        helper.setFrom("dubisoft.tech@gmail.com", "Dubisoft Tech");

//        log.debug("Mail attachment key :: " + mail.getAttachmentKey());
//        Resource resource = getAttachmentKeyResource(mail);
//        if (resource != null) {
//            log.debug("Attachment resource found :: " + resource.toString());
//            helper.addAttachment(mail.getAttachmentName(), resource);
//        }
        sender.send(mime);
//        log.info("mail sent successfully [{}], [{}]", mail.getSubject(), Utils.fromCommaStringToArray(mail.getTo())[0]);
    }
}
