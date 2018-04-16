package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.services.EmpruntService;
import bs.devweb.projet.services.MaterielService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Cette servlet permet de faire une demande d'emprunt
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/emprunt")
public class EmpruntServlet extends GenericServlet {

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
            // sinon on teste s'il a bien du materiel a emprunter
        } else if (MaterielService.getInstance().listMateriel().isEmpty()) {
            // s'il n'y en a pas, on renvoie a la page d'inventaire
            resp.sendRedirect("inventaire?emprunt=impossible");
        } else {
            // s'il y en a on recupere dans une variable etudiant, l'etudiant connecte et on l'ajoute au contexte
            Etudiant etudiant = (Etudiant) req.getSession().getAttribute("studentConnected");
            context.setVariable("user", etudiant);

            // on liste le materiel et on l'ajoute au contexte
            List<Materiel> listMateriel = MaterielService.getInstance().listMateriel();
            context.setVariable("listMateriel", listMateriel);

            // on charge la page htlm correspondante a la servlet
            templateEngine.process("emprunt", context, resp.getWriter());
        }
    }

    /**
     * Cette methode est celle utilisee lors de la validation du formulaire
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // on recupere les champs du formulaire
        String materiel = req.getParameter("stuff");
        String quantiteString = req.getParameter("quantity");
        String debutString = req.getParameter("firstday");
        String finString = req.getParameter("lastday");
        Etudiant emprunteur = (Etudiant) req.getSession().getAttribute("studentConnected");

        try{
            Integer idMateriel = Integer.parseInt(materiel);
            Materiel materielEmprunte = MaterielService.getInstance().getMaterielById(idMateriel);
            Integer quantite = Integer.parseInt(quantiteString);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate debut = LocalDate.parse(debutString, dateFormat);
            LocalDate fin = LocalDate.parse(finString, dateFormat);
            LocalDate dateActuelle = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // on verifie que la date de debut d'emprunt est bien avant la date de fin d'emprunt
            // sinon on les echange
            if (debut.isAfter(fin)) {
                LocalDate temp = debut;
                debut = fin;
                fin = temp;
            }

            Emprunt emprunt = new Emprunt(null, emprunteur, materielEmprunte, quantite, debut, fin, false, false, dateActuelle);

            // on verifie que la quantite demande est bien inferieure a la quantite de materiel existante
            // et que la quantite a emprunter est superieure positive
            if (quantite <= materielEmprunte.getQuantite() && quantite > 0) {
                // on verifie que l'emprunt est possible et que sa date de debut est au moins egale a la date actuelle
                if (EmpruntService.getInstance().checkPossibility(emprunt) && dateActuelle.isBefore(debut.plusDays(1))) {
                    // on ajoute l'attribut emprunt a la session
                    req.getSession().setAttribute("emprunt", emprunt);
                    // on renvoie a la page concernant le reglement
                    resp.sendRedirect("reglement");
                } else {
                    // on renvoie a la page d'emprunt si la periode d'emprunt n'est pas correcte
                    resp.sendRedirect("emprunt?error=period");
                }
            } else {
                // on renvoie a la page d'emprunt si la quantite demandee est incorrecte
                resp.sendRedirect("emprunt?error=quantity");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resp.sendRedirect("emprunt?error=data");
        }

    }
}
