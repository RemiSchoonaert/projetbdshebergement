package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.LieuDao;
import bs.devweb.projet.entities.Lieu;
import bs.devweb.projet.provider.DataSourceProvider;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class LieuDaoTestCase {

    private LieuDao lieuDao = new LieuDaoImpl();

    @Before
    public void initDb() throws Exception {
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM emprunt");
            stmt.executeUpdate("DELETE FROM materiel");
            stmt.executeUpdate("DELETE FROM licencier;");
            stmt.executeUpdate("DELETE FROM gerer;");
            stmt.executeUpdate("DELETE FROM association");
            stmt.executeUpdate("DELETE FROM etudiant");
            stmt.executeUpdate("DELETE FROM lieu");
            stmt.executeUpdate("DELETE FROM statut");
            stmt.executeUpdate("INSERT INTO lieu(lieu_id, lieu_nom) VALUES (1, 'Garage Jules Lefevre');");
            stmt.executeUpdate("INSERT INTO lieu(lieu_id, lieu_nom) VALUES (2, 'Local BDS');");
        }
    }

    @Test
    public void shouldGetLieu() throws Exception {
        // WHEN
        Lieu lieu = lieuDao.getLieuById(2);
        // THEN
        assertThat(lieu).isNotNull();
        assertThat(lieu.getId()).isEqualTo(2);
        assertThat(lieu.getNom()).isEqualTo("Local BDS");
    }

    @Test
    public void shouldListLieu() throws Exception {
        // WHEN
        List<Lieu> listLieu = lieuDao.listLieu();
        // THEN
        assertThat(listLieu).hasSize(2);
        assertThat(listLieu).extracting("id", "nom").containsOnly(
                tuple(1, "Garage Jules Lefevre"),
                tuple(2, "Local BDS")
        );
    }

}
