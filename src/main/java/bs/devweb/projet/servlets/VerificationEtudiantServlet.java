package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.services.EtudiantService;
import bs.devweb.projet.utils.MotDePasseUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Cette servlet est utilisee pour verifie un nouveau compte qui a ete cree (compte non verifie)
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/verifier")
public class VerificationEtudiantServlet extends GenericServlet {

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // on recupere les parametres mail et mdp de l'url
        String mail = req.getParameter("mail");
        String mdp = req.getParameter("mdp");

        // on liste les etudiants non verifies
        List<Etudiant> listEtudiant = EtudiantService.getInstance().listEtudiantNonVerifies();

        // on regarde pour chaque etudiant de cette liste si son adresse mail
        // correspond a celle recuperee et si le mot de passe correspond egalement
        for (Etudiant etudiant : listEtudiant) {
            if (etudiant.getMail().equals(mail) && MotDePasseUtils.validerMotDePasse(mdp, etudiant.getMotDePasseVerification())) {
                if (!etudiant.isVerifie()) {
                    // dans ce cas, on verifie que l'etudiant n'est pas deja verifie
                    // on l'indique comme verifie
                    etudiant.setVerifie(true);
                    // on le met a jour dans la BDD
                    EtudiantService.getInstance().updateEtudiant(etudiant);
                }
                // on set l'attribut "studentConnected" dans la session
                req.getSession().setAttribute("studentConnected", etudiant);
            }
        }

        // on renvoie a la page d'inventaire
        resp.sendRedirect("inventaire");
    }

}
