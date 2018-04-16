package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;
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
 * Cette servlet permet la designation d'un sous-responsable
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/sousresponsable")
public class SousResponsableServlet extends GenericServlet {

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
			Etudiant user = (Etudiant) req.getSession().getAttribute("studentConnected");
			context.setVariable("user", user);

			// on verifie que le statut de l'etudiant est "Responsable"
			if (user.getStatut().getNom().equals("Responsable")) {
				// on liste les associations
				List<Association> associations = AssociationService.getInstance().listAssociation();
				Integer idAssociationPresidee = null;

				// on regarde pour chaque association, laquelle est presidee par l'etudiant
				for (Association association : associations) {
					if (association.getEtudiant().getId() == user.getId()) {
						// on retient quelle est l'association presidee par l'etudiant
						idAssociationPresidee = association.getId();
					}
				}

				// cette variable contiendra les etudiants susceptibles d'etre designes "sous-responsable"
				List<Etudiant> candidats = new ArrayList<>();
				// on liste les etudiants verifies
				List<Etudiant> etudiants = EtudiantService.getInstance().listEtudiantsVerifies();
				List<Association> associationsDeEtudiant;
				List<Association> associationsGerees;
				boolean pasGeree = false;

				// on regarde chaque etudiant verifie
				for (Etudiant etudiant : etudiants) {
					// on liste les associations auxquelles appartient l'etudiant
					associationsDeEtudiant = LicencierService.getInstance().listAssociationKnowingEtudiant(etudiant);
					// on liste les associations qu'il gere
					associationsGerees = GererService.getInstance().listAssociationsGerees(etudiant);
					// on regarde chaque association a laquelle appartient l'etudiant
					for (Association association : associationsDeEtudiant) {
						// pour chaque association a laquelle il appartient,
						// on regarde s'il appartient a l'association de l'etudiant connecte
						// et on regarde s'il ne gere pas deja l'association
						if (association.getId() == idAssociationPresidee) {
							// l'etudiant appartient a l'association et peut potentiellement la gerer
							// si ne n'est pas encore le cas
							pasGeree = true;
							for (Association associationGeree : associationsGerees) {
								if (associationGeree.getId() == idAssociationPresidee) {
									// s'il la gere deja, on l'indique
									pasGeree = false;
								}
							}
						}
					}
					// si l'etudiant ne gere pas deja l'association, on l'ajoute aux candidats
					if (pasGeree) {
						candidats.add(etudiant);
					}
				}

				// on ajoute les candidats au contexte
				context.setVariable("listEtudiant", candidats);
			} else {
				// si l'etudiant n'a pas un statut "Responsable", on le renvoie a l'inventaire
				resp.sendRedirect("inventaire");
			}

			// on charge la page htlm correspondante a la servlet
			templateEngine.process("sousresponsable", context, resp.getWriter());
		}
	}

	/**
	 * Cette methode est celle utilisee lors de la validation du formulaire
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// on recupere l'id de l'etudiant designe comme "sous-responsable"
			Integer idEtudiant = Integer.parseInt(req.getParameter("student"));
			// on recupere l'etudiant a partir de son id
			Etudiant futurSousResp = EtudiantService.getInstance().getEtudiantById(idEtudiant);

			// on regarde s'il a un statut "Visiteur"
			if (futurSousResp.getStatut().getNom().equals("Visiteur")) {
				// dans ce cas, on lui donne le statut de "Sous-responsable"
				futurSousResp.setStatut(StatutService.getInstance().getStatutByName("Sous-Responsable"));
			}

			// on met a jour l'etudiant
			EtudiantService.getInstance().updateEtudiant(futurSousResp);

			// on recupere l'id de l'etudiant etant president
			Etudiant president = (Etudiant) req.getSession().getAttribute("studentConnected");
			Integer idPresident = president.getId();
			// on liste les associations
			List<Association> associations = AssociationService.getInstance().listAssociation();

			// on regarde quelle est l'association qu'il preside
			for (Association association : associations) {
				if (association.getEtudiant().getId() == idPresident) {
					// on ajoute l'etudiant "sous-responsable" comme gerant dans la BDD
					GererService.getInstance().addGerant(futurSousResp, association);
				}
			}

			// on renvoie a la meme page
			resp.sendRedirect("sousresponsable?promotion=success");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// on renvoie a la meme page
			resp.sendRedirect("sousresponsable?promotion=fail");
		}
	}

}
