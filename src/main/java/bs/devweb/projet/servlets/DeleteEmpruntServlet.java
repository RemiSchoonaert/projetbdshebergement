package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.services.EmpruntService;
import bs.devweb.projet.services.GererService;
import bs.devweb.projet.services.MailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Cette servlet est utilisee lors du refus d'une demande d'emprunt
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/emprunt/delete")
public class DeleteEmpruntServlet extends GenericServlet{

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // on regarde si l'etudiant a l'attribut "studentConnected" ou non
        if (req.getSession().getAttribute("studentConnected") != null) {
            // s'il l'a, on recupere dans une variable etudiant, l'etudiant connecte et on l'ajoute au contexte
            Etudiant etudiant = (Etudiant) req.getSession().getAttribute("studentConnected");
            // on regarde si le statut de l'etudiant est different de "visiteur"
            if (!(etudiant.getStatut().getNom().equals("Visiteur"))) {
                // s'il a un statut different de "visiteur"
                try {
                    // on recupere le parametre "id" de l'URL qui correspond a l'id de l'emprunt
                    // que l'on souhaite refuser
                    // on recupere ensuite l'emprunt concerne via l'id
                    Integer idEmpruntToDelete = Integer.parseInt(req.getParameter("id"));
                    Emprunt empruntToDelete = EmpruntService.getInstance().getEmpruntById(idEmpruntToDelete);

                    // on liste les associations gerees par l'etudiant
                    List<Association> associationsGerees = GererService.getInstance().listAssociationsGerees(etudiant);

                    // de base, on indique que l'etudiant ne peut refuser l'emprunt
                    boolean authorized = false;

                    // on verifie que l'emprunt concerne bien une association geree par l'etudiant
                    for (Association association : associationsGerees) {
                        if (association.getId() == empruntToDelete.getMateriel().getAssociation().getId()) {
                            authorized = true;
                        }
                    }

                    // si l'etudiant est autorise a refuser l'emprunt
                    if (authorized) {
                        // on applique un format aux dates
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

                        // on prepare un mail : objet, contenu
                        String objetMail = "Demande d'emprunt refusée";
                        String contenuMail = "Bonjour "+empruntToDelete.getEtudiant().getPrenom()+",\n\n"+
                                "Votre demande d'emprunt de matériel a été refusée. Elle concernait l'emprunt suivant : \n" +
                                "\t- n° d'emprunt : " + empruntToDelete.getId() +
                                "\n\t- matériel : " + empruntToDelete.getMateriel().getDesignation().toLowerCase() +
                                "\n\t- quantité : " + empruntToDelete.getQuantite() +
                                "\n\t- début : " + empruntToDelete.getDebut().format(formatter) +
                                "\n\t- fin : " + empruntToDelete.getFin().format(formatter) +
                                "\n\nSi vous souhaitez connaître la raison de ce refus, vous pouvez contacter le responsable, " + empruntToDelete.getMateriel().getAssociation().getEtudiant().getPrenom() +
                                " " + empruntToDelete.getMateriel().getAssociation().getEtudiant().getNom() + ", à l'adresse mail suivante : " + empruntToDelete.getMateriel().getAssociation().getEtudiant().getMail() +
                                "\n\n\nSi ce message ne vous est pas destiné, nous vous invitons à l'ignorer et le supprimer.\n" +
                                "Ce message est un message automatique, merci de ne pas y répondre.";
                        // on envoie ce mail a l'etudiant qui s'est vu refuse l'emprunt
                        MailService.sendMail(empruntToDelete.getEtudiant().getMail(), objetMail, contenuMail);

                        // on supprime l'emprunt de la BDD et on renvoie a la page de demandes d'emprunt
                        EmpruntService.getInstance().deleteEmprunt(empruntToDelete);
                        resp.sendRedirect("../demandes?deletion=success");
                    } else {
                        // sinon on le renvoie aux demandes d'emprunt
                        resp.sendRedirect("../demandes?deletion=fail");
                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    resp.sendRedirect("../demandes?deletion=fail");
                }
            } else {
                // si l'etudiant a un statut "visiteur", on renvoie a la page d'inventaire
                resp.sendRedirect("../inventaire");
            }
        } else {
            // si l'etudiant n'a pas l'attribut "studentConnected", on renvoie a la page de connexion
            resp.sendRedirect("../connexion");
        }
    }
}
