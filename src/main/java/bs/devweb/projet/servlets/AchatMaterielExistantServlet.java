package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.entities.Lieu;
import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.services.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Cette servlet concerne l'achat de materiel existant deja dans la BDD
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/achatmaterielexistant")
public class AchatMaterielExistantServlet extends GenericServlet{

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // On teste si l'utilisateur a un attribut "studentConnected" ou non
        if (req.getSession().getAttribute("studentConnected") == null) {
            // s'il ne l'a pas, on le renvoie a la page de connexion
            resp.sendRedirect("connexion");
        } else {
            // s'il l'a, on recupere dans une variable l'etudiant connecte et on l'ajoute au contexte
            Etudiant etudiant = (Etudiant) req.getSession().getAttribute("studentConnected");
            context.setVariable("user", etudiant);

            // on regarde le statut de l'etudiant
            if (!(etudiant.getStatut().getNom().equals("Visiteur"))) {
                // s'il a un statut different de visiteur, cela signifie qu'il est responsable ou sous-responsable

                // on liste les lieux dans une variable que l'on met dans le contexte
                List<Lieu> listLieu = LieuService.getInstance().listLieu();
                context.setVariable("listLieu", listLieu);
                // on fait de même avec les associations
                List<Association> listAssociation = GererService.getInstance().listAssociationsGerees((Etudiant) req.getSession().getAttribute("studentConnected"));
                context.setVariable("listAssociation", listAssociation);

                // on charge la page htlm correspondante a la servlet
                templateEngine.process("achatexistant", context, resp.getWriter());
            } else {
                resp.sendRedirect("inventaire");
            }
        }
    }

    /**
     * Cette methode est celle utilisee lors de la validation du formulaire
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // on regarde quel est l'id du materiel que l'on souhaite acheter
            Integer idMateriel = Integer.parseInt(req.getParameter("designation"));
            // on recupere dans une variable materielModified le materiel dont on va modofier la quantite
            Materiel materielModified = MaterielService.getInstance().getMaterielById(idMateriel);

            // on recupere la quantite initiale de materiel et celle que l'on souhaite ajoute dans 2 variables
            Integer quantiteInitiale = materielModified.getQuantite();
            Integer quantiteAjoutee = Integer.parseInt(req.getParameter("quantity"));

            // on verifie que la quantite a ajouter est bien positive
            if (quantiteAjoutee <= 0) {
                // si elle ne l'est pas, on renvoie l'etudiant sur la meme page
                resp.sendRedirect("achatmaterielexistant?achat=fail");
            } else {
                // si elle l'est, on modifie la quantite de materiel et on met a jour le materiel dans la BDD
                materielModified.setQuantite(quantiteInitiale + quantiteAjoutee);
                MaterielService.getInstance().updateMateriel(materielModified);

                // apres cela, on renvoie sur la page d'inventaire
                resp.sendRedirect("inventaire?achat=success");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resp.sendRedirect("achatmaterielexistant?achat=fail");
        }
    }

}
