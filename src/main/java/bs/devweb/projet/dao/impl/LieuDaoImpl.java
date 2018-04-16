package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.LieuDao;
import bs.devweb.projet.entities.Lieu;
import bs.devweb.projet.provider.DataSourceProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe implemente les methodes de l'interface LieuDao
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public class LieuDaoImpl implements LieuDao {

    /**
     * Cette methode retourne un lieu a partir de son id
     * @param idLieu l'id du lieu que l'on souhaite avoir en sortie
     * @return le lieu dont l'id correspond a celui indique en entree
     */
    @Override
    public Lieu getLieuById(Integer idLieu) {
        String query = "SELECT * FROM lieu WHERE lieu_id = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idLieu);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Lieu(
                            resultSet.getInt("lieu_id"),
                            resultSet.getString("lieu_nom")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette methode permet de lister tous les lieux de la BDD
     * @return la liste des lieux presents dans la BDD
     */
    @Override
    public List<Lieu> listLieu() {
        String query = "SELECT * FROM lieu;";
        List<Lieu> listLieu = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                listLieu.add(new Lieu(
                        resultSet.getInt("lieu_id"),
                        resultSet.getString("lieu_nom")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listLieu;
    }
}
