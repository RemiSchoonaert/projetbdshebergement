package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.services.EmpruntService;
import bs.devweb.projet.services.GererService;
import bs.devweb.projet.services.MailService;
import bs.devweb.projet.services.MaterielService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Cette servlet est utilisee pour indiquer la fin d'un emprunt
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/emprunt/terminate")
public class TerminateEmpruntServlet extends GenericServlet{

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // on verifie que l'attribut de session "studentConnected" n'est pas nul
        if (req.getSession().getAttribute("studentConnected") != null) {
            // on recupere l'etudiant
            Etudiant etudiant = (Etudiant) req.getSession().getAttribute("studentConnected");
            // on verifie qu'il a un statut autre que "Visiteur"
            if (!(etudiant.getStatut().getNom().equals("Visiteur"))) {
                try {
                    // on recupere l'id de l'emprunt a terminer
                    Integer idEmpruntToTerminate = Integer.parseInt(req.getParameter("id"));
                    // on recupere l'emprunt a partir de son id
                    Emprunt empruntToTerminate = EmpruntService.getInstance().getEmpruntById(idEmpruntToTerminate);
                    // on l'indique comme termine
                    empruntToTerminate.setTermine(true);

                    // on liste les associations gerees par l'etudiant
                    List<Association> associationsGerees = GererService.getInstance().listAssociationsGerees(etudiant);

                    // de base, on indique que l'etudiant ne peut terminer l'emprunt
                    boolean authorized = false;

                    // on verifie que l'emprunt concerne bien une association geree par l'etudiant
                    for (Association association : associationsGerees) {
                        if (association.getId() == empruntToTerminate.getMateriel().getAssociation().getId()) {
                            authorized = true;
                        }
                    }

                    // si l'etudiant est autorise a terminer l'emprunt
                    if (authorized) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

                        // on prepare un mail : sujet + contenu
                        String objetMail = "Emprunt terminé";
                        String contenuMail = "Bonjour "+empruntToTerminate.getEtudiant().getPrenom()+",\n\n"+
                                "Votre emprunt de matériel est terminé. Cela concernait l'emprunt suivant : \n" +
                                "\t- n° d'emprunt : " + empruntToTerminate.getId() +
                                "\n\t- matériel : " + empruntToTerminate.getMateriel().getDesignation().toLowerCase() +
                                "\n\t- quantité : " + empruntToTerminate.getQuantite() +
                                "\n\t- début : " + empruntToTerminate.getDebut().format(formatter) +
                                "\n\t- fin : " + empruntToTerminate.getFin().format(formatter) +
                                "\n\n\nSi ce message ne vous est pas destiné, nous vous invitons à l'ignorer et le supprimer.\n" +
                                "Ce message est un message automatique, merci de ne pas y répondre.";
                        // on envoie le message
                        MailService.sendMail(empruntToTerminate.getEtudiant().getMail(), objetMail, contenuMail);

                        // on met a jour l'emprunt dans la BDD
                        EmpruntService.getInstance().updateEmprunt(empruntToTerminate);

                        // on renvoie a la page des emprunts en cours
                        resp.sendRedirect("../empruntencours?termination=success");
                    } else {
                        // sinon on renvoie a la page des emprunts en cours
                        resp.sendRedirect("../empruntencours?termination=fail");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    // on renvoie a la page des emprunts en cours
                    resp.sendRedirect("../empruntencours?termination=fail");
                }
            } else {
                // si l'etudiant a un statut "visiteur", on le renvoie a la page d'inventaire
                resp.sendRedirect("../inventaire");
            }
        } else {
            // si la l'attribut "studentConnected" est nul, l'etudiant n'est pas connecte,
            // on le renvoie a la page de connexion
            resp.sendRedirect("../connexion");
        }
    }
}
