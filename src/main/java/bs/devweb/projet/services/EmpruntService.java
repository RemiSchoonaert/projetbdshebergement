package bs.devweb.projet.services;

import bs.devweb.projet.dao.EmpruntDao;
import bs.devweb.projet.dao.impl.EmpruntDaoImpl;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Materiel;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Cette classe utilise les methodes d'EmpruntDao
 * On va utiliser cette classe lorsqu'il y aura besoin d'utiliser une des methodes d'EmpruntDao
 * Elle permet, via la declaration statique d'une instance, de ne pas rajouter la ligne :
 * EmpruntDao empruntDao = new EmpruntDaoImpl();
 * a chaque fois que l'on aura besoin d'une des methodes d'EmpruntDao
 * @author SCHOONAERT Remi - BLYAU Arnold
 * @version 1.0
 */
public class EmpruntService {

    private static class EmpruntLibraryHolder {
        private final static EmpruntService instance = new EmpruntService();
    }

    public static EmpruntService getInstance() {
        return EmpruntLibraryHolder.instance;
    }

    private EmpruntService() {

    }

    EmpruntDao empruntDao = new EmpruntDaoImpl();

    public List<Emprunt> listEmpruntByAssociation(Association association) {
        return empruntDao.listEmpruntByAssociation(association);
    }

    public Emprunt getEmpruntById(Integer idEmprunt) {
        return empruntDao.getEmpruntById(idEmprunt);
    }

    public void deleteEmprunt(Emprunt emprunt) {
        empruntDao.deleteEmprunt(emprunt);
    }

    public void updateEmprunt(Emprunt emprunt) {
        empruntDao.updateEmprunt(emprunt);
    }

    public Emprunt addEmprunt(Emprunt emprunt) {
        if (emprunt == null){
            throw new IllegalArgumentException("L'emprunt ne peut être null.");
        }
        if (emprunt.getEtudiant() == null){
            throw new IllegalArgumentException("L'étudiant réalisant l'emprunt ne peut être nul.");
        }
        if (emprunt.getMateriel() == null){
            throw new IllegalArgumentException("Le matériel concerné par l'emprunt ne peut être nul.");
        }
        if (emprunt.getQuantite() == null){
            throw new IllegalArgumentException("La quantité de matériel empruntée ne peut être nulle.");
        }
        if (emprunt.getDebut() == null){
            throw new IllegalArgumentException("La date de début de l'emprunt ne peut être nulle.");
        }
        if (emprunt.getFin() == null){
            throw new IllegalArgumentException("La date de fin de l'emprunt ne peut être nulle.");
        }
        return empruntDao.addEmprunt(emprunt);
    }

    public List<Emprunt> listEmprunt(boolean valide, boolean termine) {
        return empruntDao.listEmprunt(valide, termine);
    }

    public List<Emprunt> listEmpruntConcerningMateriel(Materiel materiel) {
        return empruntDao.listEmpruntConcerningMateriel(materiel);
    }

    public boolean checkPossibility(Emprunt emprunt) {
        return empruntDao.checkPossibility(emprunt);
    }

    public HashMap<LocalDate, Integer> getAvailability(Materiel materiel, LocalDate debut, LocalDate fin) {
        return empruntDao.getAvailability(materiel, debut, fin);
    }

}
