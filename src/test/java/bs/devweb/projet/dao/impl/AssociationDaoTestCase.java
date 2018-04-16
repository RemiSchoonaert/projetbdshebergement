package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.AssociationDao;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.provider.DataSourceProvider;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class AssociationDaoTestCase {

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
        }
    }

    @Test
    public void shouldGetAssociation() throws Exception {
        // WHEN
        Association association = associationDao.getAssociationById(1);
        // THEN
        assertThat(association).isNotNull();
        assertThat(association.getId()).isEqualTo(1);
        assertThat(association.getEtudiant().getId()).isEqualTo(1);
        assertThat(association.getEtudiant().getNom()).isEqualTo("SAMANOS");
        assertThat(association.getEtudiant().getPrenom()).isEqualTo("Mathilde");
        assertThat(association.getEtudiant().getMotDePasse()).isEqualTo("azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty");
        assertThat(association.getEtudiant().getTelephone()).isEqualTo("0648745614");
        assertThat(association.getEtudiant().getMail()).isEqualTo("mathilde.samanos@hei.yncrea.fr");
        assertThat(association.getEtudiant().getStatut().getId()).isEqualTo(2);
        assertThat(association.getEtudiant().getStatut().getNom()).isEqualTo("Sous-Responsable");
        assertThat(association.getEtudiant().isVerifie()).isTrue();
        assertThat(association.getEtudiant().getMotDePasseVerification()).isEqualTo("fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320");
        assertThat(association.getNom()).isEqualTo("Rugby");
    }

    @Test
    public void shouldListAssociation() throws Exception {
        // WHEN
        List<Association> listAssociation = associationDao.listAssociation();
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

}
