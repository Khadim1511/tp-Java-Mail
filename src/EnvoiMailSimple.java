import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EnvoiMailSimple {
    public static void main(String[] args) {
        // Paramètres de connexion SMTP (exemple Gmail)
        final String username = "khadimseye2004@gmail.com";
        final String password = "gobo nsks yxpo rpaa";
        final String destinataire = "khadimseye1511@gmail.com";

        // Configuration du serveur SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authentification et session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinataire));
            message.setSubject("Test JavaMail");
            message.setText("Ceci est un message envoyé depuis un programme Java.");

            // Envoi du message
            Transport.   send(message);

            System.out.println("Message envoyé avec succès !");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}