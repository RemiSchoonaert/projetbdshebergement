package bs.devweb.projet.dao.impl;

import bs.devweb.projet.dao.AssociationDao;
import bs.devweb.projet.dao.LieuDao;
import bs.devweb.projet.dao.MaterielDao;
import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.provider.DataSourceProvider;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class MaterielDaoTestCase {

    private MaterielDao materielDao = new MaterielDaoImpl();
    private LieuDao lieuDao = new LieuDaoImpl();
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
            stmt.executeUpdate("INSERT INTO lieu(lieu_id, lieu_nom) VALUES (1, 'Garage Jules Lefevre');");
            stmt.executeUpdate("INSERT INTO lieu(lieu_id, lieu_nom) VALUES (2, 'Local BDS');");
            stmt.executeUpdate("INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (1,2,'SAMANOS', 'Mathilde', 'azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty','0648745614','mathilde.samanos@hei.yncrea.fr', 'true', 'fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320');");
            stmt.executeUpdate("INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (2,1,'DEMORY', 'Thibaut', 'titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede','0648748448','thibaut.demory@hei.yncrea.fr', 'true', 'md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF');");
            stmt.executeUpdate("INSERT INTO etudiant(etudiant_id, statut_id, etudiant_nom, prenom, mdp, telephone, mail, verifie, mdp_verification) VALUES (3,3,'EVRARD', 'William', 'aeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouyaeiouy','0632847958','william.evrard@hei.yncrea.fr', 'true', 'lff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çfezzelff0çf');");
            stmt.executeUpdate("INSERT INTO association(association_id, etudiant_id, association_nom) VALUES (1, 1, 'Rugby');");
            stmt.executeUpdate("INSERT INTO association(association_id, etudiant_id, association_nom) VALUES (2, 2, 'Foot Feminin');");
            stmt.executeUpdate("INSERT INTO materiel(materiel_id, lieu_id, association_id, designation, quantite, prix_achat) VALUES(1,1,1,'Ballons rugby', 20, 15.99);");
            stmt.executeUpdate("INSERT INTO materiel(materiel_id, lieu_id, association_id, designation, quantite, prix_achat) VALUES(2,1,2,'Crampons foot', 11, 31.0);");
            stmt.executeUpdate("INSERT INTO materiel(materiel_id, lieu_id, association_id, designation, quantite, prix_achat) VALUES(3,1,2,'Matériel non emprunté', 13, 3.23);");
            stmt.executeUpdate("INSERT INTO emprunt(emprunt_id, etudiant_id, materiel_id, quantite, emprunt_debut, emprunt_fin, valide, termine, emprunt_demande) VALUES (1,2,1,15,'2018-02-21','2018-03-21', 'false', 'false', '2018-01-10');");
            stmt.executeUpdate("INSERT INTO emprunt(emprunt_id, etudiant_id, materiel_id, quantite, emprunt_debut, emprunt_fin, valide, termine, emprunt_demande) VALUES (2,1,1,10,'2018-03-23','2018-04-11', 'false', 'false', '2018-02-10');");
            stmt.executeUpdate("INSERT INTO emprunt(emprunt_id, etudiant_id, materiel_id, quantite, emprunt_debut, emprunt_fin, valide, termine, emprunt_demande) VALUES (3,1,1,5,'2018-03-23','2018-04-11', 'false', 'false', '2018-03-10');");
        }
    }

    @Test
    public void shouldGetMateriel() throws Exception {
        // WHEN
        Materiel materiel = materielDao.getMaterielById(1);
        // THEN
        assertThat(materiel).isNotNull();
        assertThat(materiel.getId()).isEqualTo(1);
        assertThat(materiel.getLieu().getId()).isEqualTo(1);
        assertThat(materiel.getLieu().getNom()).isEqualTo("Garage Jules Lefevre");
        assertThat(materiel.getAssociation().getId()).isEqualTo(1);
        assertThat(materiel.getAssociation().getEtudiant().getId()).isEqualTo(1);
        assertThat(materiel.getAssociation().getEtudiant().getNom()).isEqualTo("SAMANOS");
        assertThat(materiel.getAssociation().getEtudiant().getPrenom()).isEqualTo("Mathilde");
        assertThat(materiel.getAssociation().getEtudiant().getMotDePasse()).isEqualTo("azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty");
        assertThat(materiel.getAssociation().getEtudiant().getTelephone()).isEqualTo("0648745614");
        assertThat(materiel.getAssociation().getEtudiant().getMail()).isEqualTo("mathilde.samanos@hei.yncrea.fr");
        assertThat(materiel.getAssociation().getEtudiant().getStatut().getId()).isEqualTo(2);
        assertThat(materiel.getAssociation().getEtudiant().getStatut().getNom()).isEqualTo("Sous-Responsable");
        assertThat(materiel.getAssociation().getEtudiant().isVerifie()).isTrue();
        assertThat(materiel.getAssociation().getEtudiant().getMotDePasseVerification()).isEqualTo("fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320");
        assertThat(materiel.getAssociation().getNom()).isEqualTo("Rugby");
        assertThat(materiel.getDesignation()).isEqualTo("Ballons rugby");
        assertThat(materiel.getQuantite()).isEqualTo(20);
        assertThat(materiel.getPrix()).isEqualTo(15.99);
    }

    @Test
    public void shouldAddMateriel() throws Exception {
        // GIVEN
        Materiel materiel = new Materiel(null, lieuDao.getLieuById(1), associationDao.getAssociationById(2),
                "Ballons", 12, 23.30);
        // WHEN
        Materiel addedMateriel = materielDao.addMateriel(materiel);
        // THEN
        assertThat(materiel).isNotNull();
        assertThat(materiel.getId()).isNotNull();
        assertThat(materiel.getId()).isGreaterThan(0);
        assertThat(materiel.getLieu().getId()).isEqualTo(1);
        assertThat(materiel.getLieu().getNom()).isEqualTo("Garage Jules Lefevre");
        assertThat(materiel.getAssociation().getId()).isEqualTo(2);
        assertThat(materiel.getAssociation().getNom()).isEqualTo("Foot Feminin");
        assertThat(materiel.getAssociation().getEtudiant().getId()).isEqualTo(2);
        assertThat(materiel.getAssociation().getEtudiant().getNom()).isEqualTo("DEMORY");
        assertThat(materiel.getAssociation().getEtudiant().getPrenom()).isEqualTo("Thibaut");
        assertThat(materiel.getAssociation().getEtudiant().getMotDePasse()).isEqualTo("titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede");
        assertThat(materiel.getAssociation().getEtudiant().getTelephone()).isEqualTo("0648748448");
        assertThat(materiel.getAssociation().getEtudiant().getMail()).isEqualTo("thibaut.demory@hei.yncrea.fr");
        assertThat(materiel.getAssociation().getEtudiant().getStatut().getId()).isEqualTo(1);
        assertThat(materiel.getAssociation().getEtudiant().getStatut().getNom()).isEqualTo("Responsable");
        assertThat(materiel.getAssociation().getEtudiant().isVerifie()).isTrue();
        assertThat(materiel.getAssociation().getEtudiant().getMotDePasseVerification()).isEqualTo("md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF");
        assertThat(materiel.getDesignation()).isEqualTo("Ballons");
        assertThat(materiel.getQuantite()).isEqualTo(12);
        assertThat(materiel.getPrix()).isEqualTo(23.30);

        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM materiel WHERE designation = 'Ballons'")) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("materiel_id")).isEqualTo(addedMateriel.getId());
                assertThat(rs.getInt("lieu_id")).isEqualTo(1);
                assertThat(rs.getInt("association_id")).isEqualTo(2);
                assertThat(rs.getInt("quantite")).isEqualTo(12);
                assertThat(rs.getDouble("prix_achat")).isEqualTo(23.30);
                assertThat(rs.next()).isFalse();
            }
        }
    }

    @Test
    public void shouldListMateriel() throws Exception {
        // WHEN
        List<Materiel> listMateriel = materielDao.listMateriel();
        // THEN
        assertThat(listMateriel).hasSize(3);
        assertThat(listMateriel).extracting("id",
                "lieu.id", "lieu.nom",
                "association.id", "association.nom",
                "association.etudiant.id", "association.etudiant.nom", "association.etudiant.prenom", "association.etudiant.motDePasse", "association.etudiant.telephone", "association.etudiant.mail",
                "association.etudiant.statut.id", "association.etudiant.statut.nom",
                "association.etudiant.verifie", "association.etudiant.motDePasseVerification",
                "designation", "quantite", "prix").containsOnly(
                tuple(1,
                        1, "Garage Jules Lefevre",
                        1, "Rugby",
                        1, "SAMANOS", "Mathilde", "azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty", "0648745614", "mathilde.samanos@hei.yncrea.fr",
                        2, "Sous-Responsable",
                        true, "fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320",
                        "Ballons rugby", 20, 15.99),
                tuple(2,
                        1, "Garage Jules Lefevre",
                        2, "Foot Feminin",
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        "Crampons foot", 11, 31.0),
                tuple(3,
                        1, "Garage Jules Lefevre",
                        2, "Foot Feminin",
                        2, "DEMORY", "Thibaut", "titidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidedetitidede", "0648748448", "thibaut.demory@hei.yncrea.fr",
                        1, "Responsable",
                        true, "md25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cFmd25d3cF",
                        "Matériel non emprunté", 13, 3.23)
        );
    }

    @Test
    public void shouldListMaterielKnowingAssociation() throws Exception {
        // GIVEN
        Association association = associationDao.getAssociationById(1);
        // WHEN
        List<Materiel> listMateriel = materielDao.listMaterielKnowingAssociation(association);
        // THEN
        assertThat(listMateriel).hasSize(1);
        assertThat(listMateriel).extracting("id",
                "lieu.id", "lieu.nom",
                "association.id", "association.nom",
                "association.etudiant.id", "association.etudiant.nom", "association.etudiant.prenom", "association.etudiant.motDePasse", "association.etudiant.telephone", "association.etudiant.mail",
                "association.etudiant.statut.id", "association.etudiant.statut.nom",
                "association.etudiant.verifie", "association.etudiant.motDePasseVerification",
                "designation", "quantite", "prix").containsOnly(
                tuple(1,
                        1, "Garage Jules Lefevre",
                        1, "Rugby",
                        1, "SAMANOS", "Mathilde", "azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty", "0648745614", "mathilde.samanos@hei.yncrea.fr",
                        2, "Sous-Responsable",
                        true, "fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320",
                        "Ballons rugby", 20, 15.99)
        );
    }

    @Test
    public void shouldUpdateMateriel() throws Exception {
        // GIVEN
        Materiel materiel = materielDao.getMaterielById(1);
        materiel.setDesignation("Nouveau matériel");
        materiel.setQuantite(4);
        materiel.setLieu(lieuDao.getLieuById(2));
        materiel.setPrix(12.03);
        // WHEN
        materielDao.updateMateriel(materiel);
        Materiel materielModified = materielDao.getMaterielById(1);
        assertThat(materielModified.getId()).isEqualTo(1);
        assertThat(materielModified.getLieu().getId()).isEqualTo(2);
        assertThat(materielModified.getLieu().getNom()).isEqualTo("Local BDS");
        assertThat(materielModified.getAssociation().getId()).isEqualTo(1);
        assertThat(materielModified.getAssociation().getEtudiant().getId()).isEqualTo(1);
        assertThat(materielModified.getAssociation().getEtudiant().getNom()).isEqualTo("SAMANOS");
        assertThat(materielModified.getAssociation().getEtudiant().getPrenom()).isEqualTo("Mathilde");
        assertThat(materielModified.getAssociation().getEtudiant().getMotDePasse()).isEqualTo("azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty");
        assertThat(materielModified.getAssociation().getEtudiant().getTelephone()).isEqualTo("0648745614");
        assertThat(materielModified.getAssociation().getEtudiant().getMail()).isEqualTo("mathilde.samanos@hei.yncrea.fr");
        assertThat(materielModified.getAssociation().getEtudiant().getStatut().getId()).isEqualTo(2);
        assertThat(materielModified.getAssociation().getEtudiant().getStatut().getNom()).isEqualTo("Sous-Responsable");
        assertThat(materielModified.getAssociation().getEtudiant().isVerifie()).isTrue();
        assertThat(materielModified.getAssociation().getEtudiant().getMotDePasseVerification()).isEqualTo("fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320");
        assertThat(materielModified.getAssociation().getNom()).isEqualTo("Rugby");
        assertThat(materielModified.getDesignation()).isEqualTo("Nouveau matériel");
        assertThat(materielModified.getQuantite()).isEqualTo(4);
        assertThat(materielModified.getPrix()).isEqualTo(12.03);
    }

    @Test
    public void shouldDeleteMateriel() throws Exception {
        // WHEN
        materielDao.deleteMateriel(materielDao.getMaterielById(1), 15);
        materielDao.deleteMateriel(materielDao.getMaterielById(3), 14);
        Materiel materiel1 = materielDao.getMaterielById(1);
        Materiel materiel2 = materielDao.getMaterielById(3);
        // THEN
        assertThat(materiel1).isNotNull();
        assertThat(materiel1.getId()).isEqualTo(1);
        assertThat(materiel1.getLieu().getId()).isEqualTo(1);
        assertThat(materiel1.getLieu().getNom()).isEqualTo("Garage Jules Lefevre");
        assertThat(materiel1.getAssociation().getId()).isEqualTo(1);
        assertThat(materiel1.getAssociation().getEtudiant().getId()).isEqualTo(1);
        assertThat(materiel1.getAssociation().getEtudiant().getNom()).isEqualTo("SAMANOS");
        assertThat(materiel1.getAssociation().getEtudiant().getPrenom()).isEqualTo("Mathilde");
        assertThat(materiel1.getAssociation().getEtudiant().getMotDePasse()).isEqualTo("azertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazertyazerty");
        assertThat(materiel1.getAssociation().getEtudiant().getTelephone()).isEqualTo("0648745614");
        assertThat(materiel1.getAssociation().getEtudiant().getMail()).isEqualTo("mathilde.samanos@hei.yncrea.fr");
        assertThat(materiel1.getAssociation().getEtudiant().getStatut().getId()).isEqualTo(2);
        assertThat(materiel1.getAssociation().getEtudiant().getStatut().getNom()).isEqualTo("Sous-Responsable");
        assertThat(materiel1.getAssociation().getEtudiant().isVerifie()).isTrue();
        assertThat(materiel1.getAssociation().getEtudiant().getMotDePasseVerification()).isEqualTo("fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320fe26D320");
        assertThat(materiel1.getAssociation().getNom()).isEqualTo("Rugby");
        assertThat(materiel1.getDesignation()).isEqualTo("Ballons rugby");
        assertThat(materiel1.getQuantite()).isEqualTo(5);
        assertThat(materiel1.getPrix()).isEqualTo(15.99);
        assertThat(materiel2).isNull();
    }

}
