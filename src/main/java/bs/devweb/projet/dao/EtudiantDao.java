package bs.devweb.projet.dao;

import bs.devweb.projet.dao.impl.EtudiantDaoImpl;
import bs.devweb.projet.entities.Etudiant;

import java.util.HashMap;
import java.util.List;

public interface EtudiantDao {

    Etudiant getEtudiantById(Integer idEtudiant);

    HashMap<String, String> listAuthorizedStudents();

    Etudiant addEtudiant(Etudiant etudiant);

    Etudiant getEtudiantByMail(String mail);

    void updateEtudiant(Etudiant etudiant);

    List<Etudiant> listEtudiant(boolean verifie);

    String genererMdpVerification();
}
