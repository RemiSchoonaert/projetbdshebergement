package bs.devweb.projet.services;

import bs.devweb.projet.dao.GererDao;
import bs.devweb.projet.dao.impl.GererDaoImpl;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;

import java.util.List;

/**
 * Cette classe utilise les methodes de GererDao
 * On va utiliser cette classe lorsqu'il y aura besoin d'utiliser une des methodes de GererDao
 * Elle permet, via la declaration statique d'une instance, de ne pas rajouter la ligne :
 * GererDao gererDao = new GererDaoImpl();
 * a chaque fois que l'on aura besoin d'une des methodes de GererDao
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class GererService {

    private static class GererLibraryHolder {
        private final static GererService instance = new GererService();
    }

    public static GererService getInstance() {
        return GererLibraryHolder.instance;
    }

    private GererService() {

    }

    GererDao gererDao = new GererDaoImpl();

    public void addGerant(Etudiant etudiant, Association association) {
        gererDao.addGerant(etudiant, association);
    }

    public List<Association> listAssociationsGerees(Etudiant etudiant) {
        return gererDao.listAssociationGerees(etudiant);
    }
}
