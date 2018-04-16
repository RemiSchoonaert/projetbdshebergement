package bs.devweb.projet.services;

import bs.devweb.projet.dao.MaterielDao;
import bs.devweb.projet.dao.impl.MaterielDaoImpl;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Materiel;

import java.util.List;

/**
 * Cette classe utilise les methodes de MaterielDao
 * On va utiliser cette classe lorsqu'il y aura besoin d'utiliser une des methodes de MaterielDao
 * Elle permet, via la declaration statique d'une instance, de ne pas rajouter la ligne :
 * MaterielDao materielDao = new MaterielDaoImpl();
 * a chaque fois que l'on aura besoin d'une des methodes de MaterielDao
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class MaterielService {

    private static class MaterielLibraryHolder {
        private final static MaterielService instance = new MaterielService();
    }

    public static MaterielService getInstance() {
        return MaterielLibraryHolder.instance;
    }

    private MaterielService() {

    }

    MaterielDao materielDao = new MaterielDaoImpl();

    public Materiel addMateriel(Materiel materiel) {
        if (materiel == null){
            throw new IllegalArgumentException("Le matériel ne peut être null.");
        }
        if (materiel.getAssociation() == null){
            throw new IllegalArgumentException("L'association propriétaire du matériel ne peut être nulle.");
        }
        if (materiel.getLieu() == null){
            throw new IllegalArgumentException("Le lieu du matériel ne peut être nul.");
        }
        if (materiel.getPrix() == null){
            throw new IllegalArgumentException("Le prix du matériel ne peut être nul.");
        }
        if (materiel.getQuantite() == null){
            throw new IllegalArgumentException("La quantité du matériel ne peut être nulle.");
        }
        if (materiel.getDesignation() == null || "".equals(materiel.getDesignation())){
            throw new IllegalArgumentException("La désignation du matériel ne peut être nulle.");
        }
        return materielDao.addMateriel(materiel);
    }

    public List<Materiel> listMateriel() {
        return materielDao.listMateriel();
    }

    public Materiel getMaterielById(Integer idMateriel) {
        return materielDao.getMaterielById(idMateriel);
    }

    public List<Materiel> listMaterielKnowingAssociation(Association association) {
        return materielDao.listMaterielKnowingAssociation(association);
    }

    public void updateMateriel(Materiel materiel) {
        materielDao.updateMateriel(materiel);
    }

    public void deleteMateriel(Materiel materiel, Integer quantite) {
        materielDao.deleteMateriel(materiel, quantite);
    }

}
