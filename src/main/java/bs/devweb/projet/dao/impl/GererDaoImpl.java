package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.AssociationDao;
import bs.devweb.projet.dao.GererDao;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.provider.DataSourceProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe implemente les methodes de l'interface GererDao
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public class GererDaoImpl implements GererDao {

    AssociationDao associationDao = new AssociationDaoImpl();

    /**
     * Cette methode permet l'ajout d'un gerant (sous-responsable) pour une association donnee
     * @param etudiant l'etudiant qui sera sous-responsable
     * @param association l'association qu'il pourra gerer
     */
    @Override
    public void addGerant(Etudiant etudiant, Association association) {
        String query = "INSERT INTO gerer(etudiant_id, association_id) VALUES (?, ?);";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, etudiant.getId());
            statement.setInt(2, association.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette methode liste les associations gerees pour un etudiant donne
     * @param etudiant l'etudiant dont on souhaite connaitre les associations gerees
     * @return la liste des associations gerees par l'etudiant
     */
    @Override
    public List<Association> listAssociationGerees(Etudiant etudiant) {
        String query = "SELECT * FROM gerer WHERE etudiant_id = ?";
        List<Association> listAssociation = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, etudiant.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    listAssociation.add(associationDao.getAssociationById(resultSet.getInt("association_id")));
                }
                return listAssociation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

