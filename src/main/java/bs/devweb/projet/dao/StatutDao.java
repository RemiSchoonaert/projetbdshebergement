package bs.devweb.projet.dao;

import bs.devweb.projet.entities.Statut;

public interface StatutDao {

    Statut getStatutById(Integer idStatut);

    Statut getStatutByName(String nomStatut);

}
