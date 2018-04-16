package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.services.*;
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
 * Cette servlet permet de declarer une perte de materiel
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/perte")
public class PerteServlet extends GenericServlet {

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

            // on verifie que l'etudiant n'a pas un statut "visiteur"
            if (!(etudiant.getStatut().getNom().equals("Visiteur"))) {
                // on liste les associations gerees par l'etudiant
                List<Association> associations = GererService.getInstance().listAssociationsGerees(etudiant);
                List<Materiel> listMateriel = new ArrayList<>();
                List<Materiel> listMaterielParAsso;
                // on liste le materiel pour chaque association geree
                for (Association association : associations) {
                    listMaterielParAsso = MaterielService.getInstance().listMaterielKnowingAssociation(association);
                    // chaque materiel de l'association est ajoute a la liste
                    for (Materiel materiel : listMaterielParAsso) {
                        listMateriel.add(materiel);
                    }
                }
                if (listMateriel.isEmpty()) {
                    // si la liste est vide, on renvoie a la page d'inventaire
                    resp.sendRedirect("inventaire?deletion=impossible");
                } else {
                    // sinon on ajoute la liste au contexte
                    // et on charge la page htlm correspondante a la servlet
                    context.setVariable("listMateriel", listMateriel);
                    templateEngine.process("perte", context, resp.getWriter());
                }
            } else {
                // si l'etudiant a le statut visiteur, on le renvoie a l'inventaire
                resp.sendRedirect("inventaire");
            }
        }
    }

    /**
     * Cette methode est celle utilisee lors de la validation du formulaire
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // on recupere le materiel concerne par la perte et la quantite a supprimer
        String materielString = req.getParameter("stuff");
        String quantiteString = req.getParameter("quantity");
        try {
            Integer materielId = Integer.parseInt(materielString);
            Materiel materiel = MaterielService.getInstance().getMaterielById(materielId);
            Integer quantiteASupprimer = Integer.parseInt(quantiteString);

            // on regarde si le materiel concerne est sous emprunt
            List<Emprunt> listEmpruntConcerningMateriel = EmpruntService.getInstance().listEmpruntConcerningMateriel(materiel);
            List<Emprunt> listEmpruntToDeleteFromHistorical = new ArrayList<>();
            boolean onlyOldEmprunt = true;

            if (listEmpruntConcerningMateriel.isEmpty()) {
                // si le materiel n'est concerne par aucun emprunt, on le supprime suivant la quantite indiquee
                MaterielService.getInstance().deleteMateriel(materiel, quantiteASupprimer);
                // on renvoie a la page d'inventaire
                resp.sendRedirect("inventaire?deletion=success");
            } else {
                // s'il est concerne par des emprunts, on recupere sa quantite totale
                Integer quantiteTotale = materiel.getQuantite();
                Integer quantiteEmpruntee = 0;
                // on regarde chaque emprunt
                for (Emprunt emprunt : listEmpruntConcerningMateriel) {
                    // si l'emprunt est termine, on l'ajoute a l'historique des emprunts
                    if (emprunt.isTermine()) {
                        listEmpruntToDeleteFromHistorical.add(emprunt);
                    } else {
                        // sinon on ajoute la quantite empruntee a la variable quantiteEmpruntee
                        quantiteEmpruntee += emprunt.getQuantite();
                        // on indique que ce materiel n'est pas concerne uniquement par des emprunts termines
                        onlyOldEmprunt = false;
                    }
                }
                // on regarde si le materiel n'est concerne que par des emprunts termines
                // et si la quantite totale est inferieure a la quantite empruntee + la quantite a supprimer
                if (onlyOldEmprunt && quantiteTotale - quantiteASupprimer - quantiteEmpruntee < 0) {
                    // dans ce cas, on va supprimer le materiel de la BDD, on supprime donc les emprunts
                    // termines qui le concernaient
                    for (Emprunt emprunt : listEmpruntToDeleteFromHistorical) {
                        EmpruntService.getInstance().deleteEmprunt(emprunt);
                    }
                    // on supprime le materiel de la BDD
                    MaterielService.getInstance().deleteMateriel(materiel, quantiteASupprimer);
                    // on renvoie a la page d'inventaire
                    resp.sendRedirect("inventaire?deletion=success");
                } else if (quantiteTotale - quantiteASupprimer - quantiteEmpruntee >= 0) {
                    // si la quantite totale est superieure a la quantite a supprimer + la quantite empruntee
                    // on supprime le materiel suivant la quantite indiquee
                    MaterielService.getInstance().deleteMateriel(materiel, quantiteASupprimer);
                    // on renvoie a la page d'inventaire
                    resp.sendRedirect("inventaire?deletion=success");
                } else {
                    // si l'emprunt est concerne par des emprunts non termines
                    // et que la quantite totale est inferieure a la quantite a supprimer + la quantite empruntee
                    // on renvoie sur la meme page
                    resp.sendRedirect("perte?deletion=unauthorized");
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resp.sendRedirect("perte?deletion=fail");
        }
    }

}
