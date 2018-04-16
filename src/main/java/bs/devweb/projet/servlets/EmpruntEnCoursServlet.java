package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.services.EmpruntService;
import bs.devweb.projet.services.GererService;
import bs.devweb.projet.services.LicencierService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette servlet concerne les emprunts valides et non termines
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/empruntencours")
public class EmpruntEnCoursServlet extends GenericServlet {

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

            // on teste si l'etudiant a un statut different de "visiteur"
            if (!(etudiant.getStatut().getNom().equals("Visiteur"))) {
                // s'il c'est le cas,
                // on cree une variable qui contiendra la liste des emprunts en cours qui seront affiches sur la page
                List<Emprunt> listEmprunt = new ArrayList<>();
                // on liste les emprunts en cours
                List<Emprunt> empruntsEnCours = EmpruntService.getInstance().listEmprunt(true, false);
                // on liste les associations gerees par l'etudiant
                List<Association> listAssociation = GererService.getInstance().listAssociationsGerees(etudiant);

                // on regarde pour chaque emprunt en cours
                for(Emprunt emprunt : empruntsEnCours) {
                    // on regarde pour chaque association geree
                    for (Association association : listAssociation) {
                        // si l'association geree est la meme que l'association concernee par l'emprunt
                        if (emprunt.getMateriel().getAssociation().getId() == association.getId()) {
                            // si c'est le cas, on ajoute l'emprunt a listEmprunt
                            listEmprunt.add(emprunt);
                        }
                    }
                }

                // si la liste des emprunts a afficher est vide
                if (listEmprunt.isEmpty()) {
                    // on ajoute au contexte un message l'indiquant
                    String alerte = "Aucun emprunt en cours.";
                    context.setVariable("alerte", alerte);
                }

                // on ajoute la liste a afficher au contexte
                context.setVariable("listEmprunt", listEmprunt);
                // on charge la page htlm correspondante a la servlet
                templateEngine.process("encours", context, resp.getWriter());
            } else {
                // si l'etudiant a un statut "visiteur", on le renvoie a la page d'inventaire
                resp.sendRedirect("inventaire");
            }
        }
    }

}
