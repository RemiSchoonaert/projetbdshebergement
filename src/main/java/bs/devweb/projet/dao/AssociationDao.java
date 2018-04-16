package bs.devweb.projet.dao;

import bs.devweb.projet.entities.Association;

import java.util.List;

public interface AssociationDao {

    Association getAssociationById(Integer idAssociation);

    List<Association> listAssociation();
}
