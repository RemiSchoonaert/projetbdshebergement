package bs.devweb.projet.services;

import bs.devweb.projet.dao.AssociationDao;
import bs.devweb.projet.dao.impl.AssociationDaoImpl;
import bs.devweb.projet.entities.Association;

import java.util.List;

/**
 * Cette classe utilise les methodes d'AssociationDao
 * On va utiliser cette classe lorsqu'il y aura besoin d'utiliser une des methodes d'AssociationDao
 * Elle permet, via la declaration statique d'une instance, de ne pas rajouter la ligne :
 * AssociationDao associationDao = new AssociationDaoImpl();
 * a chaque fois que l'on aura besoin d'une des methodes d'AssociationDao
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class AssociationService {

    private static class AssociationLibraryHolder {
        private final static AssociationService instance = new AssociationService();
    }

    public static AssociationService getInstance() {
        return AssociationLibraryHolder.instance;
    }

    private AssociationService() {

    }

    AssociationDao associationDao = new AssociationDaoImpl();

    public List<Association> listAssociation() {
        return associationDao.listAssociation();
    }

    public Association getAssociationById(Integer idAssociation) {
        return associationDao.getAssociationById(idAssociation);
    }

}
