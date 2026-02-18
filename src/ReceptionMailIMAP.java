import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.InputStream;
import java.util.Properties;

public class ReceptionMailIMAP {
    public static void main(String[] args) {
        final String username = "khadimseye2004@gmail.com";
        final String password = "gobo nsks yxpo rpaa";
        final String serveurImap = "imap.gmail.com";
        final String dossier = "INBOX";

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");

        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect(serveurImap, username, password);

            Folder inbox = store.getFolder(dossier);
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            System.out.println("Nombre de messages : " + messages.length);

            // Parcourir les 10 derniers messages
            for (int i = Math.max(0, messages.length - 10); i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("\n-------------------------------");
                System.out.println("Expéditeur : " + message.getFrom()[0]);
                System.out.println("Sujet      : " + message.getSubject());

                Object content = message.getContent();

                if (content instanceof String) {
                    System.out.println("Contenu : " + content);
                }
                else if (content instanceof Multipart) {
                    Multipart multipart = (Multipart) content;

                    for (int j = 0; j < multipart.getCount(); j++) {
                        BodyPart part = multipart.getBodyPart(j);

                        // 1. Gestion des pièces jointes (ton ajout)
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String nomFichier = part.getFileName();
                            InputStream is = part.getInputStream();
                            // Sauvegarde le fichier dans le dossier racine du projet
                            java.nio.file.Files.copy(is, java.nio.file.Paths.get(nomFichier), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                            System.out.println(">> Pièce jointe sauvegardée : " + nomFichier);
                        }
                        // 2. Affichage du contenu texte/HTML
                        else {
                            System.out.println("Partie " + j + " (" + part.getContentType() + ") :");
                            System.out.println(part.getContent());
                        }
                    }
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}