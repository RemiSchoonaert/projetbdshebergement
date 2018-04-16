package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.EtudiantDao;
import bs.devweb.projet.dao.StatutDao;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.entities.Statut;
import bs.devweb.projet.provider.DataSourceProvider;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class EtudiantDaoTestCase {

    private EtudiantDao etudiantDao = new EtudiantDaoImpl();
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
            stmt.executeUpdate("INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (1,2,'SAMANOS', 'Mathilde', 'azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty','0648745614','mathilde.samanos@hei.yncrea.fr', 'true', 'fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320');");
            stmt.executeUpdate("INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (2,1,'DEMORY', 'Thibaut', 'titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede','0648748448','thibaut.demory@hei.yncrea.fr', 'true', 'md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF');");
            stmt.executeUpdate("INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (3,3,'EVRARD', 'William', 'aeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouy','0632847958','william.evrard@hei.yncrea.fr', 'true', 'lff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çf');");
        }
    }

    @Test
    public void shouldGetEtudiantById() throws Exception {
        // WHEN
        Etudiant etudiant = etudiantDao.getEtudiantById(2);
        // THEN
        assertThat(etudiant).isNotNull();
        assertThat(etudiant.getId()).isEqualTo(2);
        assertThat(etudiant.getNom()).isEqualTo("DEMORY");
        assertThat(etudiant.getPrenom()).isEqualTo("Thibaut");
        assertThat(etudiant.getMotDePasse()).isEqualTo("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(etudiant.getTelephone()).isEqualTo("0648748448");
        assertThat(etudiant.getMail()).isEqualTo("thibaut.demory@hei.yncrea.fr");
        assertThat(etudiant.getStatut().getId()).isEqualTo(1);
        assertThat(etudiant.getStatut().getNom()).isEqualTo("Responsable");
        assertThat(etudiant.isVerifie()).isTrue();
        assertThat(etudiant.getMotDePasseVerification()).isEqualTo("md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF");
    }

    @Test
    public void shouldListAuthorizedStudents() throws Exception{
        // WHEN
        HashMap<String, String> usersAndPasswords = etudiantDao.listAuthorizedStudents();
        // THEN
        assertThat(usersAndPasswords).hasSize(3);
        assertThat(usersAndPasswords).containsKey("mathilde.samanos@hei.yncrea.fr");
        assertThat(usersAndPasswords).extracting("mathilde.samanos@hei.yncrea.fr").isNotNull();
        assertThat(usersAndPasswords).extracting("mathilde.samanos@hei.yncrea.fr").containsOnly("azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty");
        assertThat(usersAndPasswords).containsKey("thibaut.demory@hei.yncrea.fr");
        assertThat(usersAndPasswords).extracting("thibaut.demory@hei.yncrea.fr").isNotNull();
        assertThat(usersAndPasswords).extracting("thibaut.demory@hei.yncrea.fr").containsOnly("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(usersAndPasswords).containsKey("william.evrard@hei.yncrea.fr");
        assertThat(usersAndPasswords).extracting("william.evrard@hei.yncrea.fr").isNotNull();
        assertThat(usersAndPasswords).extracting("william.evrard@hei.yncrea.fr").containsOnly("aeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouy");
    }

    @Test
    public void shouldAddStudent() throws Exception {
        // GIVEN
        Etudiant etudiant = new Etudiant(null, "BLYAU", "Arnold", "BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauD",
                "arnold.blyau@hei.yncrea.fr","0652416325", statutDao.getStatutByName("Visiteur"),
                false, "kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld");
        // WHEN
        Etudiant addedStudent = etudiantDao.addEtudiant(etudiant);
        // THEN
        assertThat(addedStudent).isNotNull();
        assertThat(addedStudent.getId()).isNotNull();
        assertThat(addedStudent.getId()).isGreaterThan(0);
        assertThat(addedStudent.getNom()).isEqualTo("BLYAU");
        assertThat(addedStudent.getPrenom()).isEqualTo("Arnold");
        assertThat(addedStudent.getMotDePasse()).isEqualTo("BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauD");
        assertThat(addedStudent.getMail()).isEqualTo("arnold.blyau@hei.yncrea.fr");
        assertThat(addedStudent.getTelephone()).isEqualTo("0652416325");
        assertThat(addedStudent.getStatut().getId()).isEqualTo(3);
        assertThat(addedStudent.getStatut().getNom()).isEqualTo("Visiteur");
        assertThat(addedStudent.isVerifie()).isFalse();
        assertThat(addedStudent.getMotDePasseVerification()).isEqualTo("kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld");

        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM etudiant WHERE etudiant_nom = 'BLYAU'")) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("etudiant_id")).isEqualTo(addedStudent.getId());
                assertThat(rs.getInt("statut_id")).isEqualTo(3);
                assertThat(rs.getString("prenom")).isEqualTo("Arnold");
                assertThat(rs.getString("mdp")).isEqualTo("BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauDu59BlyauD");
                assertThat(rs.getString("mail")).isEqualTo("arnold.blyau@hei.yncrea.fr");
                assertThat(rs.getString("telephone")).isEqualTo("0652416325");
                assertThat(rs.getBoolean("verifie")).isFalse();
                assertThat(rs.getString("mdp_verification")).isEqualTo("kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld5601kiqdld");
                assertThat(rs.next()).isFalse();
            }
        }
    }

    @Test
    public void shouldGetEtudiantByMail() throws Exception {
        // WHEN
        Etudiant etudiant = etudiantDao.getEtudiantByMail("thibaut.demory@hei.yncrea.fr");
        // THEN
        assertThat(etudiant).isNotNull();
        assertThat(etudiant.getId()).isEqualTo(2);
        assertThat(etudiant.getNom()).isEqualTo("DEMORY");
        assertThat(etudiant.getPrenom()).isEqualTo("Thibaut");
        assertThat(etudiant.getMotDePasse()).isEqualTo("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(etudiant.getTelephone()).isEqualTo("0648748448");
        assertThat(etudiant.getMail()).isEqualTo("thibaut.demory@hei.yncrea.fr");
        assertThat(etudiant.getStatut().getId()).isEqualTo(1);
        assertThat(etudiant.getStatut().getNom()).isEqualTo("Responsable");
        assertThat(etudiant.isVerifie()).isTrue();
        assertThat(etudiant.getMotDePasseVerification()).isEqualTo("md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF");
    }

    @Test
    public void shouldUpdateEtudiant() throws Exception {
        // GIVEN
        Etudiant etudiant = etudiantDao.getEtudiantById(3);
        // WHEN
        etudiant.setStatut(statutDao.getStatutById(1));
        etudiantDao.updateEtudiant(etudiant);
        Etudiant modifiedEtudiant = etudiantDao.getEtudiantById(3);
        // THEN
        assertThat(modifiedEtudiant.getId()).isEqualTo(3);
        assertThat(modifiedEtudiant.getNom()).isEqualTo("EVRARD");
        assertThat(modifiedEtudiant.getPrenom()).isEqualTo("William");
        assertThat(modifiedEtudiant.getMotDePasse()).isEqualTo("aeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouy");
        assertThat(modifiedEtudiant.getTelephone()).isEqualTo("0632847958");
        assertThat(modifiedEtudiant.getMail()).isEqualTo("william.evrard@hei.yncrea.fr");
        assertThat(modifiedEtudiant.getStatut().getId()).isEqualTo(1);
        assertThat(modifiedEtudiant.getStatut().getNom()).isEqualTo("Responsable");
        assertThat(modifiedEtudiant.isVerifie()).isTrue();
        assertThat(modifiedEtudiant.getMotDePasseVerification()).isEqualTo("lff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çf");
    }

    @Test
    public void shouldListEtudiant() throws Exception {
        // WHEN
        List<Etudiant> listEtudiant = etudiantDao.listEtudiant(true);
        // THEN
        assertThat(listEtudiant).hasSize(3);
        assertThat(listEtudiant).extracting("id", "nom", "prenom", "motDePasse", "telephone", "mail",
                "statut.id", "statut.nom", "verifie", "motDePasseVerification").containsOnly(
                tuple(1, "SAMANOS", "Mathilde", "azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty", "0648745614", "mathilde.samanos@hei.yncrea.fr",
                        2, "Sous-Responsable",
                        true, "fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320"),
                tuple(2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF"),
                tuple(3, "EVRARD", "William", "aeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouy", "0632847958", "william.evrard@hei.yncrea.fr",
                        3, "Visiteur",
                        true, "lff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çf")
        );
    }

}
