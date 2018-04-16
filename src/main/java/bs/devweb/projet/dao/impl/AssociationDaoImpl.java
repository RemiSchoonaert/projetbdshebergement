package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.AssociationDao;
import bs.devweb.projet.dao.EtudiantDao;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.provider.DataSourceProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe implemente les differentes methodes de l'interface AssociationDao
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public class AssociationDaoImpl implements AssociationDao {

    private EtudiantDao etudiantDao = new EtudiantDaoImpl();

    /**
     * Cette methode prend en entree l'id d'une association et retourne l'association correspondante
     * @param idAssociation l'id de l'association que l'on souhaite avoir en sortie
     * @return l'association correspondante
     */
    @Override
    public Association getAssociationById(Integer idAssociation) {
        String query = "SELECT * FROM association WHERE association_id = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idAssociation);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Association(
                            resultSet.getInt("association_id"),
                            etudiantDao.getEtudiantById(resultSet.getInt("etudiant_id")),
                            resultSet.getString("association_nom")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode retourne la liste de toutes les associations presentes dans la BDD
     * @return la liste des associations
     */
    @Override
    public List<Association> listAssociation() {
        String query = "SELECT * FROM association;";
        List<Association> listAssociation = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                listAssociation.add(new Association(
                        resultSet.getInt("association_id"),
                        etudiantDao.getEtudiantById(resultSet.getInt("etudiant_id")),
                        resultSet.getString("association_nom")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listAssociation;
    }

}
