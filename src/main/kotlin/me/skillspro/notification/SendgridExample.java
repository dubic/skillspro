package me.skillspro.notification;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class SendgridExample {
    public static void main(String[] args) throws IOException {
        Email from = new Email("dubisoft.tech@gmail.com");
        String subject = "Test user registration email";
        Email to = new Email("udubic@gmail.com");
        Content content = new Content("text/html", "Welcome to skillspro");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
