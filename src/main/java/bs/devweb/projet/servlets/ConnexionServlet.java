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
import java.util.HashMap;

/**
 * Cette servlet concerne la partie connexion du site
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/connexion")
public class ConnexionServlet extends GenericServlet {

    // on liste les etudiants autorises (ceux qui ont verifie leur compte) dans cette variable
    private HashMap<String, String> authorizedStudents;

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // on regarde si l'etudiant a l'atttribut "studentConnected" ou non
        if (req.getSession().getAttribute("studentConnected") != null) {
            // s'il l'a deja, on le renvoie sur la page d'inventaire
            resp.sendRedirect("inventaire");
        } else {
            // s'ils ne l'a pas, on affacte les etudiants autorises a la variable
            authorizedStudents = EtudiantService.getInstance().listAuthorizedStudents();

            // on charge la page html correspondate a la servlet
            templateEngine.process("connexion", context, resp.getWriter());
        }
    }

    /**
     * Cette methode est celle utilisee lors de la validation du formulaire
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // on recupere les champs du formulaire
            String mail = req.getParameter("email");
            String mdp = req.getParameter("mdp");

            // on teste si le mail indique est bien une cle d'authorizedStudents
            // et on verifie si la valeur correspondante a cette cle et bien celle indiquee dans le champ 'mot de passe'
            if (authorizedStudents.containsKey(mail) && MotDePasseUtils.validerMotDePasse(mdp, authorizedStudents.get(mail))) {
                // si c'est la cas, on set l'attribut 'studentConnected" a la session
                // et on renvoie a la page d'inventaire
                req.getSession().setAttribute("studentConnected", EtudiantService.getInstance().getEtudiantByMail(mail));
                resp.sendRedirect("inventaire");
            } else {
                // si ce n'est pas le cas, on renvoie sur la meme page
                resp.sendRedirect("connexion?connexion=fail");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resp.sendRedirect("connexion?connexion=fail");
        }
    }
}
