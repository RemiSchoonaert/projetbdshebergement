package bs.devweb.projet.dao;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Materiel;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface EmpruntDao {

    List<Emprunt> listEmpruntByAssociation(Association association);

    Emprunt addEmprunt(Emprunt emprunt);

    void updateEmprunt(Emprunt emprunt);

    Emprunt getEmpruntById(Integer idEmprunt);

    void deleteEmprunt(Emprunt emprunt);

    List<Emprunt> listEmprunt(boolean valide, boolean termine);

    List<Emprunt> listEmpruntConcerningMateriel(Materiel materiel);

    HashMap<LocalDate, Integer> getAvailability(Materiel materiel, LocalDate debut, LocalDate fin);

    boolean checkPossibility(Emprunt emprunt);
}
