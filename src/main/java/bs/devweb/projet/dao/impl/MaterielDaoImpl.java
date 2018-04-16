package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.AssociationDao;
import bs.devweb.projet.dao.EmpruntDao;
import bs.devweb.projet.dao.LieuDao;
import bs.devweb.projet.dao.MaterielDao;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Emprunt;
import bs.devweb.projet.entities.Lieu;
import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.provider.DataSourceProvider;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Cette classe implemente les methodes de l'interface MaterielDao
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public class MaterielDaoImpl implements MaterielDao {

    private LieuDao lieuDao = new LieuDaoImpl();
    private AssociationDao associationDao = new AssociationDaoImpl();

    /**
     * Cette methode permet de retrouver un materiel a partir de son id
     * @param idMateriel l'id du materiel que l'on souhaite avoir en sortie
     * @return le materiel dont l'id correspond a celui indique en entree
     */
    @Override
    public Materiel getMaterielById(Integer idMateriel) {
        String query = "SELECT * FROM materiel WHERE materiel_id = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idMateriel);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Materiel(
                            resultSet.getInt("materiel_id"),
                            lieuDao.getLieuById(resultSet.getInt("lieu_id")),
                            associationDao.getAssociationById(resultSet.getInt("association_id")),
                            resultSet.getString("designation"),
                            resultSet.getInt("quantite"),
                            resultSet.getDouble("prix_achat")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode permet d'ajouter un materiel a la BDD
     * @param materiel le materiel que l'on souhaite ajouter
     * @return le materiel ajoute
     */
    @Override
    public Materiel addMateriel(Materiel materiel) {
        String query = "INSERT INTO materiel(lieu_id, association_id, designation, quantite, prix_achat) VALUES(?, ?, ?, ?, ?);";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, materiel.getLieu().getId());
            statement.setInt(2, materiel.getAssociation().getId());
            statement.setString(3, materiel.getDesignation());
            statement.setInt(4, materiel.getQuantite());
            statement.setDouble(5, materiel.getPrix());
            statement.executeUpdate();
            try (ResultSet ids = statement.getGeneratedKeys()) {
                if (ids.next()) {
                    int generatedId = ids.getInt(1);
                    materiel.setId(generatedId);
                    return materiel;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode liste tout le materiel present dans la BDD
     * @return la liste du materiel present dans la BDD
     */
    @Override
    public List<Materiel> listMateriel() {
        String query = "SELECT * FROM materiel;";
        List<Materiel> listMateriel = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                listMateriel.add(new Materiel(
                                resultSet.getInt("materiel_id"),
                                lieuDao.getLieuById(resultSet.getInt("lieu_id")),
                                associationDao.getAssociationById(resultSet.getInt("association_id")),
                                resultSet.getString("designation"),
                                resultSet.getInt("quantite"),
                                resultSet.getDouble("prix_achat")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listMateriel;
    }

    /**
     * Cette methode retourne le materiel appartenant a une association
     * @param association l'association dont on souhaite connaitre le materiel detenu
     * @return la liste du materiel detenu par l'association
     */
    @Override
    public List<Materiel> listMaterielKnowingAssociation(Association association) {
        String query = "SELECT * FROM materiel WHERE association_id=?;";
        List<Materiel> listMateriel = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, association.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    listMateriel.add(
                            new Materiel(
                                    resultSet.getInt("materiel_id"),
                                    lieuDao.getLieuById(resultSet.getInt("lieu_id")),
                                    associationDao.getAssociationById(resultSet.getInt("association_id")),
                                    resultSet.getString("designation"),
                                    resultSet.getInt("quantite"),
                                    resultSet.getDouble("prix_achat")
                            )
                    );
                }
                return listMateriel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode permet de mettre a jour les caracteristiques d'un materiel donne
     * @param materiel le materiel dont on souhaite mettre a jour les caracteristiques
     */
    @Override
    public void updateMateriel(Materiel materiel) {
        String query = "UPDATE materiel SET lieu_id=?, association_id=?, designation=?, quantite=?, prix_achat=? " +
                "WHERE materiel_id=?;";
        try {
            Connection connection = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, materiel.getLieu().getId());
            stmt.setInt(2, materiel.getAssociation().getId());
            stmt.setString(3, materiel.getDesignation());
            stmt.setInt(4, materiel.getQuantite());
            stmt.setDouble(5, materiel.getPrix());
            stmt.setInt(6, materiel.getId());
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cette methode permet modifier la quantite d'un materiel donne
     * ou le supprimer si cette quantite est superieure a sa quantite initiale
     * @param materiel le materiel dont on souhaite modifier la quantite ou supprimer
     * @param quantite la quantite que l'on supprime
     */
    @Override
    public void deleteMateriel(Materiel materiel, Integer quantite) {
        Integer newQuantity = materiel.getQuantite() - quantite;
        if (newQuantity < 0) {
            String query = "DELETE FROM materiel WHERE materiel_id=?;";
            try {
                Connection connection = DataSourceProvider.getDataSource().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, materiel.getId());
                stmt.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            String query = "UPDATE materiel SET lieu_id=?, association_id=?, designation=?, quantite=?, prix_achat=? " +
                    "WHERE materiel_id=?;";
            try {
                Connection connection = DataSourceProvider.getDataSource().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, materiel.getLieu().getId());
                stmt.setInt(2, materiel.getAssociation().getId());
                stmt.setString(3, materiel.getDesignation());
                stmt.setInt(4, newQuantity);
                stmt.setDouble(5, materiel.getPrix());
                stmt.setInt(6, materiel.getId());
                stmt.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}

