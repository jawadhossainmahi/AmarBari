package Util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class SendEmail {

    /**
     * Sends an email and returns a Response object indicating success or failure.
     */
    public static Response sendMail(String toEmail, String subject, String body) {
        final String username = "jawadhossainmahi@gmail.com";
        final String password = "adnmemfkompwfado"; 

        // 1. Setup SMTP Server Properties
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); 

        // 2. Create the Session
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 3. Compose the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // 4. Send the email
            Transport.send(message);

            // Return success response
            return new Response(true, "Email sent successfully to " + toEmail);

        } catch (MessagingException e) {
            // Return failure response with the error message
            return new Response(false, "Failed to send email: " + e.getMessage());
        }
    }
}