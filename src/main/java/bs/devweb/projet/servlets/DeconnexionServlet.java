package bs.devweb.projet.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cette servlet concerne la partie deconnexion du site
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/deconnexion")
public class DeconnexionServlet extends GenericServlet {

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // on set les attributs de la session a null et on renvoie a la page de connexion
        req.getSession().setAttribute("studentConnected", null);
        req.getSession().setAttribute("emprunt", null);
        resp.sendRedirect("connexion");
    }

}
