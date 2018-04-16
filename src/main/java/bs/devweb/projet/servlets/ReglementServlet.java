package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.services.EmpruntService;
import bs.devweb.projet.services.MailService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

/**
 * Cette servlet concerne le reglement a accepter lors d'une demande d'emprunt
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/reglement")
public class ReglementServlet extends GenericServlet {

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // on teste si l'utilisateur a un attribut "studentConnected" ou non
        // et qu'un attribut "emprunt" existe en session
        if (req.getSession().getAttribute("studentConnected") == null || req.getSession().getAttribute("emprunt") == null) {
            // si ça n'est pas le cas, on renvoie a la page de connexion
            resp.sendRedirect("connexion");
        } else {
            // si c'est le cas, on recupere dans une variable etudiant, l'etudiant connecte
            // et on l'ajoute au contexte
            Etudiant etudiant = (Etudiant) req.getSession().getAttribute("studentConnected");
            context.setVariable("user", etudiant);

            // on recupere dans une variable l'emprunt et on l'ajoute au contexte
            Emprunt emprunt = (Emprunt) req.getSession().getAttribute("emprunt");
            context.setVariable("emprunt", emprunt);

            // on calcule le cout de l'emprunt et on l'ajoute au contexte
            Double coutEmprunt = emprunt.getQuantite() * emprunt.getMateriel().getPrix();
            String cout2Decimales = (new BigDecimal(coutEmprunt.toString()).setScale(2, BigDecimal.ROUND_HALF_UP)).toString();
            context.setVariable("coutEmprunt", cout2Decimales);

            // on charge la page htlm correspondante a la servlet
            templateEngine.process("reglement", context, resp.getWriter());
        }
    }

    /**
     * Cette methode est celle utilisee lors de la validation du formulaire
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // on regarde si la case d'approbation concernant l'emprunt a ete cochee
        String acceptation = req.getParameter("accept");
        // on recupere l'attribut "emprunt" de la session dans une variable
        Emprunt emprunt = (Emprunt) req.getSession().getAttribute("emprunt");

        // on verifie que la case a ete cochee
        if (acceptation != null) {
            try {
                // on ajoute l'emprunt dans la BDD
                EmpruntService.getInstance().addEmprunt(emprunt);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

                // on prepare un mail : sujet + contenu
                String objetMail = "Récapitulatif de votre demande d'emprunt";
                String contenuMail = "Bonjour "+emprunt.getEtudiant().getPrenom()+",\n\n"+"Vous avez demandé à faire un emprunt de matériel " +
                        "sur Matériel BDS. Voici un récapitulatif de votre demande :\n" +
                        "\t- n° d'emprunt : " + emprunt.getId() +
                        "\n\t- matériel : " + emprunt.getMateriel().getDesignation().toLowerCase() +
                        "\n\t- quantité : " + emprunt.getQuantite() +
                        "\n\t- début : " + emprunt.getDebut().format(formatter) +
                        "\n\t- fin : " + emprunt.getFin().format(formatter) +
                        "\n\nLe président de l'association \"" + emprunt.getMateriel().getAssociation().getNom() + "\" en a été prévenu " +
                        "et vous serez averti par message de sa décision." +
                        "\n\n\nSi ce message ne vous est pas destiné, nous vous invitons à l'ignorer et le supprimer.\n" +
                        "Ce message est un message automatique, merci de ne pas y répondre.";
                // on envoie le mail a l'etudiant ayant demande l'emprunt
                MailService.sendMail(emprunt.getEtudiant().getMail(), objetMail, contenuMail);

                // on prepare un autre message
                objetMail = "Demande d'emprunt";
                contenuMail = "Bonjour "+emprunt.getMateriel().getAssociation().getEtudiant().getPrenom()+",\n\n"+
                        emprunt.getEtudiant().getPrenom() + " " + emprunt.getEtudiant().getNom() + " a demandé à faire un emprunt de matériel " +
                        "auprès de votre association. Voici un récapitulatif de sa demande :\n" +
                        "\t- n° d'emprunt : " + emprunt.getId() +
                        "\n\t- matériel : " + emprunt.getMateriel().getDesignation().toLowerCase() +
                        "\n\t- quantité : " + emprunt.getQuantite() +
                        "\n\t- début : " + emprunt.getDebut().format(formatter) +
                        "\n\t- fin : " + emprunt.getFin().format(formatter) +
                        "\n\nPour lui faire part de votre décision concernant cet emprunt, connectez-vous sur Matériel BDS et rendez-vous dans la section \"Emprunt\" => \"Consulter les demandes\"." +
                        "\n\nSi vous souhaitez contacter directement " + emprunt.getEtudiant().getPrenom() + " pour discuter de sa demande, cela est possible : " +
                        "\n\t- via son adresse mail : " + emprunt.getEtudiant().getMail() +
                        "\n\t- via son numéro de téléphone : " + emprunt.getEtudiant().getTelephone() +
                        "\n\n\nSi ce message ne vous est pas destiné, nous vous invitons à l'ignorer et le supprimer.\n" +
                        "Ce message est un message automatique, merci de ne pas y répondre.";
                // on envoie le mail au president de l'association a laquelle appartient le materiel
                MailService.sendMail(emprunt.getMateriel().getAssociation().getEtudiant().getMail(), objetMail, contenuMail);

                // on reset l'attribut "emprunt" de la session
                req.getSession().setAttribute("emprunt", null);
                // on renvoie sur la page d'inventaire
                resp.sendRedirect("inventaire?emprunt=success");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                // on renvoie sur la page de reglement
                resp.sendRedirect("reglement?approval=fail");
            }
        } else {
            // on renvoie sur la page de reglement car le reglement n'a pas ete approuve
            resp.sendRedirect("reglement?approval=notapproved");
        }
    }

}

