package bs.devweb.projet.dao;

import bs.devweb.projet.entities.Lieu;

import java.util.List;

public interface LieuDao {

    Lieu getLieuById(Integer idLieu);

    List<Lieu> listLieu();

}
