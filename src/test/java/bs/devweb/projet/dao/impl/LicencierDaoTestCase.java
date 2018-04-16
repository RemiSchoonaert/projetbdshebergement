package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.AssociationDao;
import bs.devweb.projet.dao.EtudiantDao;
import bs.devweb.projet.dao.LicencierDao;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Etudiant;
import bs.devweb.projet.provider.DataSourceProvider;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class LicencierDaoTestCase {

    private LicencierDao licencierDao = new LicencierDaoImpl();
    private EtudiantDao etudiantDao = new EtudiantDaoImpl();
    private AssociationDao associationDao = new AssociationDaoImpl();

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
            stmt.executeUpdate("INSERT INTO association(association_id, etudiant_id, association_nom) VALUES (1, 1, 'Rugby');");
            stmt.executeUpdate("INSERT INTO association(association_id, etudiant_id, association_nom) VALUES (2, 2, 'Foot Feminin');");
            stmt.executeUpdate("INSERT INTO licencier(etudiant_id, association_id) VALUES (1,2);");
            stmt.executeUpdate("INSERT INTO licencier(etudiant_id, association_id) VALUES (2,1);");
            stmt.executeUpdate("INSERT INTO licencier(etudiant_id, association_id) VALUES (2,2);");
            stmt.executeUpdate("INSERT INTO licencier(etudiant_id, association_id) VALUES (3,2);");
        }
    }

    @Test
    public void shouldListAssociationKnowingEtudiant() throws Exception {
        // WHEN
        List<Association> listAssociation = licencierDao.listAssociationKnowingEtudiant(etudiantDao.getEtudiantById(2));
        // THEN
        assertThat(listAssociation).hasSize(2);
        assertThat(listAssociation).extracting("id",
                "etudiant.id", "etudiant.nom", "etudiant.prenom", "etudiant.motDePasse", "etudiant.telephone", "etudiant.mail",
                "etudiant.statut.id", "etudiant.statut.nom",
                "etudiant.verifie", "etudiant.motDePasseVerification",
                "nom").containsOnly(
                tuple(1,
                        1, "SAMANOS", "Mathilde", "azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty", "0648745614", "mathilde.samanos@hei.yncrea.fr",
                        2, "Sous-Responsable",
                        true, "fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320",
                        "Rugby"),
                tuple(2,
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        "Foot Feminin")
        );
    }

    @Test
    public void shouldListEtudiantKnowingAssociation() throws Exception {
        // WHEN
        List<Etudiant> listEtudiant = licencierDao.listEtudiantKnowingAssociation(associationDao.getAssociationById(2));
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

    @Test
    public void shouldAddStudentAssociations() throws Exception {
        // GIVEN
        Etudiant etudiant = etudiantDao.getEtudiantById(3);
        String[] associationIds = new String[]{"1"};
        // WHEN
        licencierDao.addStudentAssociations(etudiant, associationIds);
        List<Association> studentAssos = licencierDao.listAssociationKnowingEtudiant(etudiant);
        // THEN
        assertThat(studentAssos).hasSize(2);
        assertThat(studentAssos).extracting("id",
                "etudiant.id", "etudiant.nom", "etudiant.prenom", "etudiant.motDePasse", "etudiant.telephone", "etudiant.mail",
                "etudiant.statut.id", "etudiant.statut.nom",
                "etudiant.verifie", "etudiant.motDePasseVerification",
                "nom").containsOnly(
                tuple(1,
                        1, "SAMANOS", "Mathilde", "azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty", "0648745614", "mathilde.samanos@hei.yncrea.fr",
                        2, "Sous-Responsable",
                        true, "fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320",
                        "Rugby"),
                tuple(2,
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        "Foot Feminin")
        );
    }

}
