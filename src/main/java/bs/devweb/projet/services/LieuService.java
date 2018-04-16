package bs.devweb.projet.services;

import bs.devweb.projet.dao.LieuDao;
import bs.devweb.projet.dao.impl.LieuDaoImpl;
import bs.devweb.projet.entities.Lieu;

import java.util.List;

/**
 * Cette classe utilise les methodes de LieuDao
 * On va utiliser cette classe lorsqu'il y aura besoin d'utiliser une des methodes de LieuDao
 * Elle permet, via la declaration statique d'une instance, de ne pas rajouter la ligne :
 * LieuDao lieuDao = new LieuDaoImpl();
 * a chaque fois que l'on aura besoin d'une des methodes de LieuDao
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class LieuService {

    private static class LieuLibraryHolder {
        private final static LieuService instance = new LieuService();
    }

    public static LieuService getInstance() {
        return LieuLibraryHolder.instance;
    }

    private LieuService() {

    }

    LieuDao lieuDao = new LieuDaoImpl();

    public List<Lieu> listLieu() {
        return lieuDao.listLieu();
    }

    public Lieu getLieuById(Integer idLieu) {
        return lieuDao.getLieuById(idLieu);
    }

}
