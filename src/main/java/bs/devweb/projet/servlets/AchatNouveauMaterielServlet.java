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
 * Cette servlet concerne l'achat de nouveau materiel (non present dans la BDD)
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/achatnouveaumateriel")
public class AchatNouveauMaterielServlet extends GenericServlet {

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // on teste si l'utilisateur a un attribut "studentConnected" ou non
        if (req.getSession().getAttribute("studentConnected") == null) {
            // s'il n'a pas l'attribut, on le renvoie à la page de connexion
            resp.sendRedirect("connexion");
        } else {
            // s'il l'a, on recupere dans une variable etudiant, l'etudiant connecte et on l'ajoute au contexte
            Etudiant etudiant = (Etudiant) req.getSession().getAttribute("studentConnected");
            context.setVariable("user", etudiant);

            // on regarde le statut de l'etudiant
            if (!(etudiant.getStatut().getNom().equals("Visiteur"))) {
                // s'il a un statut different de visiteur (sous-responsable ou responsable)

                // on liste les lieux de la BDD dans une variable que l'on met dans le contexte
                List<Lieu> listLieu = LieuService.getInstance().listLieu();
                context.setVariable("listLieu", listLieu);

                // on fait de même avec les associations
                List<Association> listAssociation = GererService.getInstance().listAssociationsGerees((Etudiant) req.getSession().getAttribute("studentConnected"));
                context.setVariable("listAssociation", listAssociation);

                // on charge la page htlm correspondante a la servlet
                templateEngine.process("nouvelachat", context, resp.getWriter());
            } else {
                // s'il a le statut "visiteur", on le renvoie sur la page de l'inventaire
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
            // on recupere dans des variables les differents champs du formulaire d'achat
            Integer idAssociation = Integer.parseInt(req.getParameter("association"));
            String designation = req.getParameter("designation");
            Integer idLieu = Integer.parseInt(req.getParameter("location"));
            Integer quantite = Integer.parseInt(req.getParameter("quantity"));
            Double prix = Double.parseDouble(req.getParameter("price"));

            // on verifie que la quantite et le prix d'achat sont superieurs a 0
            if (quantite > 0 && prix > 0) {
                // s'ils le sont, on recupere l'association pour laquelle il a eu un achat de materiel
                // ainsi que le lieu dans lequel sera stocke le materiel
                Association association = AssociationService.getInstance().getAssociationById(idAssociation);
                Lieu lieu = LieuService.getInstance().getLieuById(idLieu);
                // on cree un nouveau materiel
                Materiel materiel = new Materiel(null, lieu, association, designation, quantite, prix);

                // on l'ajoute dans la BDD et on retourne sur la page d'inventaire
                MaterielService.getInstance().addMateriel(materiel);
                resp.sendRedirect("inventaire?achat=success");
            } else {
                // si le prix ou la quantite est incorrect, on renvoie sur la page
                resp.sendRedirect("achatnouveaumateriel?error=data");
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resp.sendRedirect("achatnouveaumateriel?error=data");
        }
    }
}
