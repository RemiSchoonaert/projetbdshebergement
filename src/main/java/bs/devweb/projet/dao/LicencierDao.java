package bs.devweb.projet.dao;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;

import java.util.HashMap;
import java.util.List;

public interface LicencierDao {

    List<Association> listAssociationKnowingEtudiant(Etudiant etudiant);

    List<Etudiant> listEtudiantKnowingAssociation(Association association);

    void addStudentAssociations(Etudiant etudiant, String[] associations);
}
