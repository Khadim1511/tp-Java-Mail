import jakarta.mail.*;

import java.io.InputStream; // Nécessaire pour la lecture des fichiers
import java.util.Properties;

class tp_pop3_lecture {
    public static void main(String[] args) {
        final String username = "khadimseye2004@gmail.com";
        final String password = "gobo nsks yxpo rpaa";
        final String pop3Host = "pop.gmail.com";
        final int pop3Port = 995;

        Properties props = new Properties();
        props.put("mail.pop3.host", pop3Host);
        props.put("mail.pop3.port", String.valueOf(pop3Port));
        props.put("mail.pop3.ssl.enable", "true");
        props.put("mail.pop3.auth", "true");

        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("pop3s");
            store.connect(pop3Host, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            System.out.println("Connexion POP3 réussie. Lecture des messages...");

            Message[] messages = inbox.getMessages();
            int nbAfficher = Math.min(5, messages.length);

            for (int i = messages.length - nbAfficher; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("\n==============================");
                System.out.println("Sujet : " + message.getSubject());

                Object contenu = message.getContent();

                if (contenu instanceof String) {
                    System.out.println("Contenu texte : " + contenu);
                }
                else if (contenu instanceof Multipart) {
                    Multipart mp = (Multipart) contenu;
                    for (int j = 0; j < mp.getCount(); j++) {
                        BodyPart part = mp.getBodyPart(j);

                        // --- GESTION ET SAUVEGARDE DES PIÈCES JOINTES ---
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String nomFichier = part.getFileName();

                            // On récupère le flux de données du fichier
                            InputStream is = part.getInputStream();

                            // Sauvegarde physique sur le disque (racine du projet)
                            java.nio.file.Files.copy(is,
                                    java.nio.file.Paths.get(nomFichier),
                                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                            System.out.println(">> Pièce jointe enregistrée : " + nomFichier);
                        } else {
                            // Affichage du texte normal du mail
                            System.out.println("Partie " + j + " texte : " + part.getContent());
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