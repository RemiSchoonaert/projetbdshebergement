package bs.devweb.projet.dao;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Materiel;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface MaterielDao {

    Materiel getMaterielById(Integer idMateriel);

    Materiel addMateriel(Materiel materiel);

    List<Materiel> listMateriel();

    List<Materiel> listMaterielKnowingAssociation(Association association);

    void updateMateriel(Materiel materiel);

    void deleteMateriel(Materiel materiel, Integer quantite);
}
