package bs.devweb.projet.servlets;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.entities.Statut;
import bs.devweb.projet.services.*;
import bs.devweb.projet.utils.MotDePasseUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Cette servlet concerne l'inscription
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
@WebServlet("/inscription")
public class InscriptionServlet extends GenericServlet {

    /**
     * Cette methode est celle utilisee au chargement de la servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // on teste si l'utilisateur a un attribut "studentConnected" ou non
        if (req.getSession().getAttribute("studentConnected") != null) {
            // s'il l'a, on le renvoie a la page d'inventaire
            resp.sendRedirect("inventaire");
        } else {
            // s'il ne l'a pas, on liste les associations et on les ajoute au contexte
            List<Association> listAssociation = AssociationService.getInstance().listAssociation();
            context.setVariable("listAssociation", listAssociation);
        }

        // on charge la page htlm correspondante a la servlet
        templateEngine.process("inscription", context, resp.getWriter());
    }

    /**
     * Cette methode est celle utilisee lors de la validation du formulaire
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // on recupere les champs du formulaire
        String nom = req.getParameter("name").toUpperCase(); // on met le nom en majuscules
        String prenom = req.getParameter("surname");
        String mdp = req.getParameter("mdp");
        String cfmmdp = req.getParameter("cfmmdp");
        String[] idAssociation = req.getParameterValues("association");
        String mail = req.getParameter("email");
        String telephone = req.getParameter("phone");

        // on met la 1ere lettre du prenom en majuscule
        char[] prenomDecompose = prenom.toCharArray();
        if (prenomDecompose.length > 0) {
            prenomDecompose[0] = Character.toUpperCase(prenomDecompose[0]);
            prenom = new String(prenomDecompose);
        }
        Statut basicStatut = StatutService.getInstance().getStatutByName("Visiteur");
        String mdpVerification = EtudiantService.getInstance().genererMdpConfirmation();
        // on liste les etudiants verifies et non verifies
        List<Etudiant> etudiantsVerifies = EtudiantService.getInstance().listEtudiantsVerifies();
        List<Etudiant> etudiantsNonVerifies = EtudiantService.getInstance().listEtudiantNonVerifies();
        boolean etudiantExistant = false;
        Etudiant etudiant = new Etudiant(null, nom, prenom, MotDePasseUtils.genererMotDePasse(mdp), mail, telephone, basicStatut, false, MotDePasseUtils.genererMotDePasse(mdpVerification));

        try {
            // on verifie que le mot de passe et celui de confirmation sont identiques
            // et que le mot de passe indique n'est pas nul
            if (!nom.equals("")) {
                if (!prenom.equals("")) {
                    if (mdp.equals(cfmmdp) && mdp != "") {
                        // on verifie que le mail est du type "@hei.yncrea.fr"
                        if (mail.contains("@hei.yncrea.fr")) {
                            // on regarde pour chaque etudiant verifie, si son adresse mail correspond a celle indiquee
                            // dans le formulaire
                            for (Etudiant etudiantTemp : etudiantsVerifies) {
                                if (etudiantTemp.getMail().equals(etudiant.getMail())) {
                                    // si c'est le cas, on indique que l'etudiant existe deja
                                    etudiantExistant = true;
                                }
                            }
                            // on regarde pour chaque etudiant non verifie, si son adresse mail correspond a celle indiquee
                            // dans le formulaire
                            for (Etudiant etudiantTemp : etudiantsNonVerifies) {
                                if (etudiantTemp.getMail().equals(etudiant.getMail())) {
                                    // si c'est le cas, on indique que l'etudiant existe deja
                                    etudiantExistant = true;
                                }
                            }
                            // si l'etudiant n'existe pas deja
                            if (!etudiantExistant) {
                                // on verifie que son numero de telephone est correct : 10 chiffres et commencant par 0
                                if (telephone.length() == 10 && telephone.charAt(0) == '0') {
                                    // si c'est la cas, on ajoute l'etudiant a la BDD
                                    // et on indique egalement les associations auxquelles il appartient
                                    Etudiant addedStudent = EtudiantService.getInstance().addEtudiant(etudiant);
                                    LicencierService.getInstance().addStudentAssociations(addedStudent, idAssociation);

                                    // on prepare un mail : objet et contenu
                                    String objetMail = "Confirmation d'inscription";
                                    String contenuMail = "Bonjour "+addedStudent.getPrenom()+",\n\n"+"Vous avez demandé à vous inscrire " +
                                            "sur Matériel BDS. Vous trouverez ci-dessous le lien sur lequel nous invitons à cliquer et vous " +
                                            "permettant de vérifier votre compte :\n" +
                                            "\nVoici votre lien : http://localhost:8080/projet/verifier?mail="+mail+"&mdp="+mdpVerification+
                                            "\n\n\nSi ce message ne vous est pas destiné, nous vous invitons à l'ignorer et le supprimer.\n" +
                                            "Ce message est un message automatique, merci de ne pas y répondre.";
                                    // on envoie le mail
                                    MailService.sendMail(mail, objetMail, contenuMail);

                                    // on renvoie a la page de connexion
                                    resp.sendRedirect("connexion?inscription=success");
                                } else {
                                    // on renvoie a la page d'inscription car le telephone est errone
                                    resp.sendRedirect("inscription?error=phone");
                                }
                            } else {
                                // on renvoie a la page d'inscription car l'adresse mail est erronee
                                resp.sendRedirect("inscription?error=existingmail");
                            }
                        }
                        else {
                            // on renvoie a la page d'inscription car le mail ne repond pas aux criteres
                            resp.sendRedirect("inscription?error=mail");
                        }
                    } else {
                        // on renvoie a la page d'inscription car le mot de passe et celui de confirmation sont differents
                        resp.sendRedirect("inscription?error=pwd");
                    }
                } else {
                    // on renvoie a la page d'inscrition car le prenom n'est pas renseigne
                    resp.sendRedirect("inscription?error=firstname");
                }
            } else {
                // on renvoie a la page d'inscription car le nom n'est pas renseigne
                resp.sendRedirect("inscription?error=lastname");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resp.sendRedirect("inscription?error=data");
        }
    }

}
