package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.services.EtudiantService;
import bs.devweb.projet.utils.MotDePasseUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cette servlet permet la modification de son mot de passe
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/moncompte")
public class MonCompteServlet extends GenericServlet {

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
            // s'il y en a on recupere dans une variable etudiant, l'etudiant connecte et on l'ajoute au contexte
            Etudiant etudiant = (Etudiant) req.getSession().getAttribute("studentConnected");
            context.setVariable("user", etudiant);

            // on charge la page htlm correspondante a la servlet
            templateEngine.process("moncompte", context, resp.getWriter());
        }
    }

    /**
     * Cette methode est celle utilisee lors de la validation du formulaire
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // on recupere les champs du formulaire
        Etudiant etudiant = (Etudiant) req.getSession().getAttribute("studentConnected");
        String mdpActuel = req.getParameter("ancienmdp");
        String nouveauMdp = req.getParameter("newmdp");
        String mdpConfirmation = req.getParameter("cfmmdp");

        // on verifie que le nouveau mot de passe n'est pas nul, que le nouveau mot de passe est le meme que celui
        // de confirmation et que le mot de passe actuel est le bon
        if (nouveauMdp != "" && nouveauMdp.equals(mdpConfirmation) && MotDePasseUtils.validerMotDePasse(mdpActuel, etudiant.getMotDePasse())) {
            // on set le nouveau mot de passe de l'etudiant
            etudiant.setMotDePasse(MotDePasseUtils.genererMotDePasse(nouveauMdp));
            // on met a jour l'etudiant dans la BDD
            EtudiantService.getInstance().updateEtudiant(etudiant);
            // on renvoie sur la meme page
            resp.sendRedirect("moncompte?changepwd=success");
        } else {
            // on renvoie sur la meme page
            resp.sendRedirect("moncompte?changepwd=fail");
        }
    }

}
