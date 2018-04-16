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
 * Cette servlet concerne les demandes d'emprunt
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/demandes")
public class DemandesServlet extends GenericServlet {

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
                // si c'est le cas, on liste les associations gerees par l'etudiant
                List<Association> listAssociation = GererService.getInstance().listAssociationsGerees(etudiant);
                // on cree la liste des emprunts que l'etudiant pourra gerer
                List<Emprunt> listEmprunt = new ArrayList<>();
                List<Emprunt> listEmpruntTemp;
                // on regarde pour chaque association geree, les emprunts qui la concernent
                for (Association association : listAssociation) {
                    // on recupere dans une liste temporaire les emprunts pour chaque association
                    listEmpruntTemp = EmpruntService.getInstance().listEmpruntByAssociation(association);
                    for (Emprunt emprunt : listEmpruntTemp) {
                        // on regarde pour chaque emprunt de cette liste s'il n'a pas deja ete valide
                        if (!(emprunt.isValide())) {
                            // dans le cas ou il n'a pas ete valide, on le rajoute a listEmprunt
                            listEmprunt.add(emprunt);
                        }
                    }
                }

                // on regarde si la liste des demandes d'emprunt n'est pas vide
                if (listEmprunt.isEmpty()) {
                    // si elle l'est, on ajoute au contexte un message l'indiquant
                    String alerte = "Aucune demande d'emprunt.";
                    context.setVariable("alerte", alerte);
                }

                // on ajoute au contexte, la liste des emprunts
                context.setVariable("listEmprunt", listEmprunt);
                // on charge la page htlm correspondante a la servlet
                templateEngine.process("demandes", context, resp.getWriter());
            } else {
                // si l'etudiant a un statut "visiteur", on le renvoie a la page d'inventaire
                resp.sendRedirect("inventaire");
            }
        }
    }

}
