package bs.devweb.projet.dao;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;

import java.util.List;

public interface GererDao {

    void addGerant(Etudiant etudiant, Association association);

    List<Association> listAssociationGerees(Etudiant etudiant);

}

