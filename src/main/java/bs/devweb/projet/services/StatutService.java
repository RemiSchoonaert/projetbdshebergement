package bs.devweb.projet.services;

import bs.devweb.projet.dao.StatutDao;
import bs.devweb.projet.dao.impl.StatutDaoImpl;
import bs.devweb.projet.entities.Statut;

/**
 * Cette classe utilise les methodes de StatutDao
 * On va utiliser cette classe lorsqu'il y aura besoin d'utiliser une des methodes de StatutDao
 * Elle permet, via la declaration statique d'une instance, de ne pas rajouter la ligne :
 * StatutDao statutDao = new StatutDaoImpl();
 * a chaque fois que l'on aura besoin d'une des methodes de StatutDao
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class StatutService {

    private static class StatutLibraryHolder {
        private final static StatutService instance = new StatutService();
    }

    public static StatutService getInstance() {
        return StatutLibraryHolder.instance;
    }

    private StatutService() {

    }

    StatutDao statutDao = new StatutDaoImpl();

    public Statut getStatutByName(String nomStatut) {
        return statutDao.getStatutByName(nomStatut);
    }

}
