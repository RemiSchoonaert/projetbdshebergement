package bs.devweb.projet.services;

import bs.devweb.projet.dao.LicencierDao;
import bs.devweb.projet.dao.impl.LicencierDaoImpl;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;

import java.util.List;

/**
 * Cette classe utilise les methodes de LicencierDao
 * On va utiliser cette classe lorsqu'il y aura besoin d'utiliser une des methodes de LicencierDao
 * Elle permet, via la declaration statique d'une instance, de ne pas rajouter la ligne :
 * LicencierDao licencierDao = new LicencierDaoImpl();
 * a chaque fois que l'on aura besoin d'une des methodes de LicencierDao
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class LicencierService {

    private static class LicencierLibraryHolder {
        private final static LicencierService instance = new LicencierService();
    }

    public static LicencierService getInstance() {
        return LicencierLibraryHolder.instance;
    }

    private LicencierService() {

    }

    LicencierDao licencierDao = new LicencierDaoImpl();

    public List<Association> listAssociationKnowingEtudiant(Etudiant etudiant) {
        return licencierDao.listAssociationKnowingEtudiant(etudiant);
    }

    public void addStudentAssociations(Etudiant etudiant, String[] associations) {
        licencierDao.addStudentAssociations(etudiant, associations);
    }

}
