package bs.devweb.projet.servlets;

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
import java.util.*;

/**
 * Cette servlet concerne la page d'accueil une fois connecte, l'inventaire
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/inventaire")
public class InventaireServlet extends GenericServlet {

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

            // on recupere le materiel de la BDD dans une liste
            List<Materiel> listMateriel = MaterielService.getInstance().listMateriel();
            String alerte = "";
            // on verifie que la liste de materiel n'est pas vide
            if (listMateriel.isEmpty()) {
                // si elle l'est on ajoute une alerte
                 alerte = "Il n'est possible d'emprunter aucun matériel pour le moment.";
            }
            // on ajoute l'alerte au contexte
            context.setVariable("alerte", alerte);

            HashMap<LocalDate, Integer> disponibilite;
            LocalDate dateActuelle = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int ajoutJour;
            Integer quantiteDisponible;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            String aAfficher;
            HashMap<Materiel, String> informations = new LinkedHashMap<>();

            // on regarde chaque materiel de la liste
            for (Materiel materiel : listMateriel) {
                // on verifie que la quantite de materiel totale est bien positive
                if (materiel.getQuantite() > 0) {
                    // on reset ajoutJour a 0
                    ajoutJour = 0;
                    // on recupere les disponibilites du materiel a la date actuelle
                    disponibilite = EmpruntService.getInstance().getAvailability(materiel, dateActuelle, dateActuelle);
                    // on recupere la quantite disponible a la date actuelle
                    quantiteDisponible = disponibilite.get(dateActuelle);

                    // si cette quantite est strictement superieure a 1
                    if (quantiteDisponible > 1) {
                        // on indique ce qui sera affiche dans ce cas
                        aAfficher = String.format("%s disponibles aujourd'hui", quantiteDisponible);
                        // si elle est egale a 1
                    } else if (quantiteDisponible == 1) {
                        // on indique ce qui sera affiche dans ce cas
                        aAfficher = String.format("%s disponible aujourd'hui", quantiteDisponible);
                    } else {
                        // tant que la quantite disponible est nulle
                        while (quantiteDisponible <= 0) {
                            // on regarde la quantite disponible le jour suivant
                            ajoutJour++;
                            disponibilite = EmpruntService.getInstance().getAvailability(materiel, dateActuelle.plusDays(ajoutJour), dateActuelle.plusDays(ajoutJour));
                            quantiteDisponible = disponibilite.get(dateActuelle.plusDays(ajoutJour));
                        }
                        // on regarde si la quantite disponible a la date trouvee est strictement superieure a 1
                        if (quantiteDisponible > 1) {
                            // on indique ce qui sera affiche dans ce cas
                            aAfficher = String.format("%s disponibles à partir du ", quantiteDisponible) + dateActuelle.plusDays(ajoutJour).format(formatter);
                        } else {
                            // sinon indique ce qui sera affiche dans ce cas
                            aAfficher = String.format("%s disponible à partir du ", quantiteDisponible) + dateActuelle.plusDays(ajoutJour).format(formatter);
                        }
                    }
                    // on ajoute dans la hashmap pour chaque materiel, le message a afficher
                    informations.put(materiel, aAfficher);
                } else {
                    // si le materiel existe mais qu'il n'y en a pas en stock on l'indique
                    informations.put(materiel, "Indisponible pour une durée indéterminée");
                }
            }
            // on ajoute les informations a afficher au contexte
            context.setVariable("listMateriel", informations);

            // on charge la page htlm correspondante a la servlet
            templateEngine.process("inventaire", context, resp.getWriter());
        }
    }

}
