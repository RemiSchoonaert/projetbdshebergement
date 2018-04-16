package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.services.CalendrierService;
import bs.devweb.projet.services.MaterielService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cette servlet affichera les disponibilites d'un materiel donne
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/disponibilite")
public class DisponibiliteServlet extends GenericServlet {

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

            try {
                // on recupere le parametre correspondant a l'id du materiel
                Integer idMateriel = Integer.parseInt(req.getParameter("materiel"));
                // on recupere le materiel concerne a partir de l'id
                Materiel materiel = MaterielService.getInstance().getMaterielById(idMateriel);
                // on recupere la periode sur laquelle afficher la disponibilite du materiel
                Integer periode = Integer.parseInt(req.getParameter("periode"));
                // on teste si l'id correspond bien a un id de materiel de la BDD
                if (materiel != null) {
                    // si c'est le cas, on ajoute au contexte le materiel
                    // ainsi que le calendrier des disponibilites et la periode choisie
                    context.setVariable("materiel", materiel);
                    context.setVariable("calendrier", CalendrierService.generateCalendar(idMateriel, periode));
                    context.setVariable("periode", periode);

                    // on charge la page htlm correspondante a la servlet
                    templateEngine.process("disponibilite", context, resp.getWriter());
                } else {
                    // si l'id du materiel n'existe pas, on renvoie sur la page d'inventaire
                    resp.sendRedirect("inventaire?calendarerror=id");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                resp.sendRedirect("inventaire?calendarerror=id");
            }

        }
    }

}
