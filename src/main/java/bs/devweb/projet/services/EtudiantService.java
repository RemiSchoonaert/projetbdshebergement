package bs.devweb.projet.services;

import bs.devweb.projet.dao.EtudiantDao;
import bs.devweb.projet.dao.impl.EtudiantDaoImpl;
import bs.devweb.projet.entities.Etudiant;

import java.util.HashMap;
import java.util.List;

/**
 * Cette classe utilise les methodes d'EtudiantDao
 * On va utiliser cette classe lorsqu'il y aura besoin d'utiliser une des methodes d'EtudiantDao
 * Elle permet, via la declaration statique d'une instance, de ne pas rajouter la ligne :
 * EtudiantDao etudiantDao = new EtudiantDaoImpl();
 * a chaque fois que l'on aura besoin d'une des methodes d'EtudiantDao
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class EtudiantService {

    private static class EtudiantLibraryHolder {
        private final static EtudiantService instance = new EtudiantService();
    }

    public static EtudiantService getInstance() {
        return EtudiantLibraryHolder.instance;
    }

    private EtudiantService() {

    }

    EtudiantDao etudiantDao = new EtudiantDaoImpl();

    public Etudiant getEtudiantById(Integer idEtudiant) {
        return etudiantDao.getEtudiantById(idEtudiant);
    }

    public HashMap<String, String> listAuthorizedStudents() {
        return etudiantDao.listAuthorizedStudents();
    }

    public Etudiant addEtudiant(Etudiant etudiant) {
        if (etudiant == null){
            throw new IllegalArgumentException("L'étudiant ne peut être null.");
        }
        if (etudiant.getStatut() == null){
            throw new IllegalArgumentException("Le statut de l'étudiant ne peut être nul.");
        }
        if (etudiant.getNom() == null || "".equals(etudiant.getNom())){
            throw new IllegalArgumentException("Le nom de l'étudiant ne peut être nul.");
        }
        if (etudiant.getPrenom() == null || "".equals(etudiant.getPrenom())){
            throw new IllegalArgumentException("Le prénom de l'étudiant ne peut être nul.");
        }
        if (etudiant.getMotDePasse() == null || "".equals(etudiant.getMotDePasse())){
            throw new IllegalArgumentException("Le mot de passe de l'étudiant ne peut être nul.");
        }
        if (etudiant.getMail() == null || "".equals(etudiant.getMail())){
            throw new IllegalArgumentException("Le mail de l'étudiant ne peut être nul.");
        }
        if (etudiant.getTelephone() == null || "".equals(etudiant.getTelephone())){
            throw new IllegalArgumentException("Le téléphone de l'étudiant ne peut être nul.");
        }
        return etudiantDao.addEtudiant(etudiant);
    }

    public Etudiant getEtudiantByMail(String mail) {
        return etudiantDao.getEtudiantByMail(mail);
    }

    public void updateEtudiant(Etudiant etudiant) {
        etudiantDao.updateEtudiant(etudiant);
    }

    public List<Etudiant> listEtudiantsVerifies() {
        return etudiantDao.listEtudiant(true);
    }

    public List<Etudiant> listEtudiantNonVerifies() {
        return etudiantDao.listEtudiant(false);
    }

    public String genererMdpConfirmation() {
        return etudiantDao.genererMdpVerification();
    }

}
