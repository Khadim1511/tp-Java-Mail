import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.io.File;

public class EnvoiMailAvecPJ {
    public static void main(String[] args) {
        final String username = "khadimseye2004@gmail.com";
        final String password = "gobo nsks yxpo rpaa";
        final String destinataire = "khadimseye1511@gmail.com";
        final String cheminPJ = "src/matchday.png";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinataire));
            message.setSubject("Envoi de fichier via JavaMail");

            // Corps du mail (texte)
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Veuillez trouver en pièce jointe le document demandé.");

            // Pièce jointe
            MimeBodyPart pieceJointe = new MimeBodyPart();
            pieceJointe.attachFile(new File(cheminPJ));

            // Regrouper texte + PJ dans un multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(pieceJointe);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Mail avec pièce jointe envoyé !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}