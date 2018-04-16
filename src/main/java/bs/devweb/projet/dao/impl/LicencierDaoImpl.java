package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.AssociationDao;
import bs.devweb.projet.dao.EtudiantDao;
import bs.devweb.projet.dao.LicencierDao;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.provider.DataSourceProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Cette classe implemente les methodes de l'interface LicencierDao
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public class LicencierDaoImpl implements LicencierDao {

    AssociationDao associationDao = new AssociationDaoImpl();
    EtudiantDao etudiantDao = new EtudiantDaoImpl();

    /**
     * Cette methode liste les associations auxquelles appartient un etudiant
     * @param etudiant l'etudiant dont on souhaite savoir quelles sont les associations auxquelles il appartient
     * @return la liste des associations auxquelles appartient l'etudiant
     */
    @Override
    public List<Association> listAssociationKnowingEtudiant(Etudiant etudiant) {
        String query = "SELECT * FROM licencier WHERE etudiant_id = ?";
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

    /**
     * Cette methode liste les etudiants appartenant a une association
     * @param association l'association dont on souhaite connaitre les membres
     * @return la liste des etudiants appartenant a l'association
     */
    @Override
    public List<Etudiant> listEtudiantKnowingAssociation(Association association) {
        String query = "SELECT * FROM licencier WHERE association_id = ?";
        List<Etudiant> listEtudiant = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, association.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    listEtudiant.add(etudiantDao.getEtudiantById(resultSet.getInt("etudiant_id")));
                }
                return listEtudiant;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode ajoute pour un etudiant, les associations auxquelles il appartient dans la BDD
     * @param etudiant l'etudiant pour lequel on va indiquer les associations auxquelles il appartient
     * @param associations les associations auxquelles appartient l'etudiant
     */
    @Override
    public void addStudentAssociations(Etudiant etudiant, String[] associations) {
        String query = "INSERT INTO licencier(etudiant_id, association_id) VALUES(?, ?);";
        if (associations != null) {
            for (int i = 0; i < associations.length; i++) {
                try (Connection connection = DataSourceProvider.getDataSource().getConnection();
                     PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, etudiant.getId());
                    statement.setInt(2, associationDao.getAssociationById(Integer.parseInt(associations[i])).getId());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
