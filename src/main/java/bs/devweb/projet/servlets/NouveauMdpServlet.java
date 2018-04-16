package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.services.EtudiantService;
import bs.devweb.projet.services.MailService;
import bs.devweb.projet.utils.MotDePasseUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Cette servlet permet d'obtenir un nouveau mot de passe lorsqu'il a ete oublie
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/nouveaumdp")
public class NouveauMdpServlet extends GenericServlet {

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // on teste si l'utilisateur a un attribut "studentConnected" ou non
        if (req.getSession().getAttribute("studentConnected") != null) {
            // s'il l'a, on le renvoie a la page d'inventaire
            resp.sendRedirect("inventaire");
        } else {
            // s'il ne l'a pas,
            // on charge la page htlm correspondante a la servlet
            templateEngine.process("nouveaumdp", context, resp.getWriter());
        }
    }

    /**
     * Cette methode est celle utilisee lors de la validation du formulaire
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // on recupere le mail pour lequel le mot de passe a ete oublie
        String mail = req.getParameter("email");

        // on liste les etudiant verifies
        List<Etudiant> etudiants = EtudiantService.getInstance().listEtudiantsVerifies();
        Etudiant etudiantAModifier = null;
        // on regarde, pour chaque etudiant verifie, si son mail correspond a celui indique dans le formulaire
        for (Etudiant etudiant : etudiants) {
            if (etudiant.getMail().equals(mail)) {
                // si c'est le cas, on recupere l'etudiant concerne dans une variable
                etudiantAModifier = etudiant;
            }
        }

        // on regarde si la boucle a permis de trouver un etudiant ou non
        if (etudiantAModifier != null) {
            // on genere un nouveau mdp
            String nouveauMdp = EtudiantService.getInstance().genererMdpConfirmation();
            // on set ce nouveau mot de passe pour l'etudiant en question
            etudiantAModifier.setMotDePasse(MotDePasseUtils.genererMotDePasse(nouveauMdp));
            // on met a jour l'etudiant dans la BDD
            EtudiantService.getInstance().updateEtudiant(etudiantAModifier);
            // on prepare un mail : sujet + contenu
            String objetMail = "Nouveau mot de passe";
            String contenuMail = "Bonjour "+etudiantAModifier.getPrenom()+",\n\n"+
                    "Vous avez oublié votre mot de passe et demandé à le faire modifier. Vous en trouverez un nouveau ci-dessous " +
                    "que nous vous invitons à modifier lors de votre prochaine connexion."+
                    "\n\nVoici votre nouveau mot de passe : "+nouveauMdp+
                    "\n\n\nSi ce message ne vous est pas destiné, nous vous invitons à l'ignorer et le supprimer.\n" +
                    "Ce message est un message automatique, merci de ne pas y répondre.";
            // on envoie le mail
            MailService.sendMail(mail, objetMail, contenuMail);
            // on renvoie a la page de connexion
            resp.sendRedirect("connexion?changepwd=success");
        } else {
            // on renvoie a la page actuelle si le mail indique ne correspond pas a un etudiant de la BDD
            resp.sendRedirect("nouveaumdp?changepwd=fail");
        }

    }
}
