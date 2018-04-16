package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.StatutDao;
import bs.devweb.projet.entities.Statut;
import bs.devweb.projet.provider.DataSourceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Cette classe implemente les methodes de l'interface StatutDao
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public class StatutDaoImpl implements StatutDao {

    /**
     * Cette methode retourne un statut a partir de son id
     * @param idStatut l'id du statut que l'on souhaite avoir en sortie
     * @return le statut dont l'id correspond a celui d'entree
     */
    @Override
    public Statut getStatutById(Integer idStatut) {
        String query = "SELECT * FROM statut WHERE statut_id = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idStatut);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Statut(
                            resultSet.getInt("statut_id"),
                            resultSet.getString("statut_nom")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode retourne un statut a partir de son nom
     * @param nomStatut le nom du statut que l'on souhaite avoir en sortie
     * @return le statut dont le nom correspond a celui indique en entree
     */
    @Override
    public Statut getStatutByName(String nomStatut) {
        String query = "SELECT * FROM statut WHERE statut_nom = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nomStatut);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Statut(
                            resultSet.getInt("statut_id"),
                            resultSet.getString("statut_nom")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
