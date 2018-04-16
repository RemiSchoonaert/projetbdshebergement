package bs.devweb.projet.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Cette classe permet l'envoi de messages via sa methode statique :
 * sendMail(String destinataire, String objet, String contenu)
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class MailService {

    public static void sendMail(String destinataire, String objet, String contenu) {
        // on indique l'adresse mail et le mot de passe du compte gmail
        // qui sera utilise pour l'envoi de messages
        final String username = "automail.materielbds@gmail.com";
        final String password = "MTdkA74m4L07";

        // on ajoute les proprietes
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        // on indique le serveur utilise, son port
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // on recupere la session grace a l'authentification
        // "on se connecte sur le compte gmail"
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // on cree un nouveau message
            Message message = new MimeMessage(session);
            // on indique son adresse mail de provenance
            message.setFrom(new InternetAddress(username));
            // on indique le destinataire
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinataire));
            // on indique l'objet
            message.setSubject(objet);
            // on indique le contenu
            message.setText(contenu);
            // on envoie le message
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}
