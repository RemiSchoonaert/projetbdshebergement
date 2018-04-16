package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.StatutDao;
import bs.devweb.projet.entities.Statut;
import bs.devweb.projet.provider.DataSourceProvider;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class StatutDaoTestCase {
    private StatutDao statutDao = new StatutDaoImpl();

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
            stmt.executeUpdate("INSERT INTO statut (statut_id, statut_nom) VALUES (1,'Responsable');");
            stmt.executeUpdate("INSERT INTO statut (statut_id, statut_nom) VALUES (2,'Sous-Responsable');");
            stmt.executeUpdate("INSERT INTO statut (statut_id, statut_nom) VALUES (3,'Visiteur');");
        }
    }

    @Test
    public void shouldGetStatutById() throws Exception {
        // WHEN
        Statut statut = statutDao.getStatutById(3);
        // THEN
        assertThat(statut).isNotNull();
        assertThat(statut.getId()).isEqualTo(3);
        assertThat(statut.getNom()).isEqualTo("Visiteur");
    }

    @Test
    public void shouldGetStatutByName() throws Exception {
        // WHEN
        Statut statut = statutDao.getStatutByName("Visiteur");
        // THEN
        assertThat(statut).isNotNull();
        assertThat(statut.getId()).isEqualTo(3);
        assertThat(statut.getNom()).isEqualTo("Visiteur");
    }

}
